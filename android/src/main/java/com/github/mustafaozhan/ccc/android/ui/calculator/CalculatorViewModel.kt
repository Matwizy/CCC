/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.ccc.android.model.DataState
import com.github.mustafaozhan.ccc.android.util.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.android.util.MutableSingleLiveData
import com.github.mustafaozhan.ccc.android.util.SingleLiveData
import com.github.mustafaozhan.ccc.android.util.calculateResult
import com.github.mustafaozhan.ccc.android.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.android.util.getFormatted
import com.github.mustafaozhan.ccc.android.util.isRewardExpired
import com.github.mustafaozhan.ccc.android.util.removeUnUsedCurrencies
import com.github.mustafaozhan.ccc.android.util.toPercent
import com.github.mustafaozhan.ccc.android.util.toRates
import com.github.mustafaozhan.ccc.android.util.toSupportedCharacters
import com.github.mustafaozhan.ccc.android.util.toUnit
import com.github.mustafaozhan.ccc.client.repo.SettingsRepository
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.kermit
import com.github.mustafaozhan.ccc.common.model.Currency
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mariuszgromada.math.mxparser.Expression

@Suppress("TooManyFunctions")
class CalculatorViewModel(
    private val settingsRepository: SettingsRepository,
    private val apiRepository: ApiRepository,
    private val currencyDao: CurrencyDao,
    private val offlineRatesDao: OfflineRatesDao
) : ViewModel(), CalculatorEvent {

    companion object {
        private const val MAXIMUM_INPUT = 18
        private const val CHAR_DOT = '.'
        const val KEY_DEL = "DEL"
        const val KEY_AC = "AC"
    }

    // region SEED
    private val _state = MutableCalculatorState()
    val state = CalculatorState(_state)

    private val _effect = MutableSingleLiveData<CalculatorEffect>()
    val effect: SingleLiveData<CalculatorEffect> = _effect

    val data = CalculatorData()

    fun getEvent() = this as CalculatorEvent
    // endregion

    init {
        with(_state) {
            _base.value = settingsRepository.currentBase
            _input.value = ""

            _base.addSource(state.base) {
                currentBaseChanged(it)
            }
            _input.addSource(state.input) { input ->
                _loading.value = true
                calculateOutput(input)
            }

            viewModelScope.launch {
                currencyDao.collectActiveCurrencies()
                    .map { it.removeUnUsedCurrencies() }
                    .collect { _currencyList.value = it }
            }
        }
    }

    private fun getRates() = data.rates?.let { rates ->
        calculateConversions(rates)
        _state._dataState.value = DataState.Cached(rates.date)
    } ?: viewModelScope.launch {
        apiRepository
            .getRatesByBase(settingsRepository.currentBase)
            .execute(
                ::rateDownloadSuccess,
                ::rateDownloadFail
            )
    }

    private fun rateDownloadSuccess(currencyResponse: CurrencyResponse) = viewModelScope.launch {
        currencyResponse.toRates().let {
            data.rates = it
            calculateConversions(it)
            _state._dataState.value = DataState.Online(it.date)
            offlineRatesDao.insertOfflineRates(it)
        }
    }.toUnit()

    private fun rateDownloadFail(t: Throwable) = viewModelScope.launch {
        kermit.w(t) { "rate download failed." }

        offlineRatesDao.getOfflineRatesByBase(
            settingsRepository.currentBase
        )?.let { offlineRates ->
            calculateConversions(offlineRates)
            _state._dataState.value = DataState.Offline(offlineRates.date)
        } ?: run {
            kermit.w(t) { "no offline rate found" }
            state.currencyList.value?.size
                ?.whether { it > 1 }
                ?.let { _effect.postValue(ErrorEffect) }
            _state._dataState.value = DataState.Error
        }
    }.toUnit()

    private fun calculateOutput(input: String) =
        Expression(input.toSupportedCharacters().toPercent())
            .calculate()
            .mapTo { if (isNaN()) "" else getFormatted() }
            .whether { length <= MAXIMUM_INPUT }
            ?.let { output ->
                _state._output.value = output
                state.currencyList.value?.size
                    ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
                    ?.whetherNot { state.input.value.isNullOrEmpty() }
                    ?.let { _effect.postValue(FewCurrencyEffect) }
                    ?: run { getRates() }
            } ?: run {
            _effect.postValue(MaximumInputEffect)
            _state._input.value = input.dropLast(1)
            _state._loading.value = false
        }

    private fun calculateConversions(rates: Rates?) = with(_state) {
        _currencyList.value = _currencyList.value?.onEach {
            it.rate = rates.calculateResult(it.name, _output.value)
        }
        _loading.value = false
    }

    private fun currentBaseChanged(newBase: String) {
        data.rates = null
        settingsRepository.currentBase = newBase

        _state._input.value = _state._input.value

        viewModelScope.launch {
            _state._symbol.value = currencyDao.getCurrencyByName(newBase)?.symbol ?: ""
        }
    }

    fun verifyCurrentBase(it: String) {
        _state._base.postValue(it)
    }

    fun getCurrentBase() = settingsRepository.currentBase

    fun isRewardExpired() = settingsRepository.adFreeActivatedDate.isRewardExpired()

    // region Event
    override fun onKeyPress(key: String) {
        when (key) {
            KEY_AC -> {
                _state._input.value = ""
                _state._output.value = ""
            }
            KEY_DEL -> state.input.value
                ?.whetherNot { isEmpty() }
                ?.apply {
                    _state._input.value = substring(0, length - 1)
                }
            else -> _state._input.value =
                if (key.isEmpty()) "" else state.input.value.toString() + key
        }
    }

    override fun onItemClick(currency: Currency, conversion: String) = with(_state) {
        var finalResult = conversion

        while (finalResult.length > MAXIMUM_INPUT) {
            finalResult = finalResult.dropLast(1)
        }

        if (finalResult.last() == CHAR_DOT) {
            finalResult = finalResult.dropLast(1)
        }

        _base.value = currency.name
        _input.value = finalResult
    }

    override fun onItemLongClick(currency: Currency): Boolean {
        _effect.postValue(
            ShowRateEffect(
                currency.getCurrencyConversionByRate(settingsRepository.currentBase, data.rates),
                currency.name
            )
        )
        return true
    }

    override fun onBarClick() = _effect.postValue(OpenBarEffect)

    override fun onSpinnerItemSelected(base: String) {
        _state._base.value = base
    }

    override fun onSettingsClicked() = _effect.postValue(OpenSettingsEffect)
    // endregion
}
