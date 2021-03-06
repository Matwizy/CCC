/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.repo

import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.russhwolf.settings.Settings
import kotlinx.datetime.Clock

class SettingsRepository(private val settings: Settings) {
    companion object {
        private const val KEY_FIRST_RUN_COUNTER = "key_run_counter"
        private const val KEY_FIRST_RUN = "firs_run"
        private const val KEY_CURRENT_BASE = "current_base"
        private const val KEY_AD_FREE_DATE = "ad_free_date"
        private const val KEY_APP_THEME = "app_theme"
        private const val KEY_LAST_REVIEW_REQUEST = "last_review_request"
    }

    var runCounter
        get() = settings.getInt(KEY_FIRST_RUN_COUNTER, 0)
        set(value) = settings.putInt(KEY_FIRST_RUN_COUNTER, value)

    var firstRun
        get() = settings.getBoolean(KEY_FIRST_RUN, true)
        set(value) = settings.putBoolean(KEY_FIRST_RUN, value)

    var currentBase
        get() = settings.getString(KEY_CURRENT_BASE, CurrencyType.NULL.toString())
        set(value) = settings.putString(KEY_CURRENT_BASE, value)

    var appTheme
        get() = settings.getInt(KEY_APP_THEME, -1)
        set(value) = settings.putInt(KEY_APP_THEME, value)

    var adFreeActivatedDate
        get() = settings.getLong(KEY_AD_FREE_DATE, 0.toLong())
        set(value) = settings.putLong(KEY_AD_FREE_DATE, value)

    var lastReviewRequest: Long
        get() = settings.getLong(KEY_LAST_REVIEW_REQUEST, Clock.System.now().toEpochMilliseconds())
        set(value) = settings.putLong(KEY_LAST_REVIEW_REQUEST, value)
}
