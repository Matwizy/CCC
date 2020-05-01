/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyDao
import mustafaozhan.github.com.mycurrencies.data.room.offlineRates.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates

@Database(entities = [(Currency::class), (Rates::class)], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    abstract fun offlineRatesDao(): OfflineRatesDao
}
