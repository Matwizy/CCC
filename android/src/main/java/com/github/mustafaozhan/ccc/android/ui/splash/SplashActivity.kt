/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.github.mustafaozhan.ccc.android.base.activity.BaseActivity
import com.github.mustafaozhan.ccc.android.ui.main.MainActivity
import com.github.mustafaozhan.ccc.android.ui.slider.SliderActivity
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(preferencesRepository.appTheme)

        startActivity(
            Intent(
                this,
                if (preferencesRepository.firstRun) {
                    SliderActivity::class.java
                } else {
                    MainActivity::class.java
                }
            )
        )

        finish()
    }
}