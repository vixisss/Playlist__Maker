package com.example.playlist__maker.presentation

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate


private const val EXAMPLE_PREFERENCES = "shared_preferences"
private const val SWITCH_THEME_KEY = "key_of_switch_theme"


class App : Application() {
    var darkTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        val themeSharedPreferences = getSharedPreferences(EXAMPLE_PREFERENCES, MODE_PRIVATE)
        val getThemeSharedPreferences = themeSharedPreferences.getBoolean(SWITCH_THEME_KEY, darkTheme)
        switchTheme(getThemeSharedPreferences)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}