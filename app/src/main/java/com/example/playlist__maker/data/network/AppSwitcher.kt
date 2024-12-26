package com.example.playlist__maker.data.network

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class AppSwitcher (
    private val app: Application,
    private val preferencesShared: SharedPreferences
){
    companion object{
        private const val SWITCH_THEME_KEY = "key_of_switch_theme"
    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        saveTheme(darkThemeEnabled)
    }

    fun saveTheme(theme: Boolean) {
        preferencesShared.edit()
            .putBoolean(SWITCH_THEME_KEY, theme)
            .apply()
    }

    private fun themeFromDevice(): Boolean = app.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    fun getTheme(): Boolean = preferencesShared.getBoolean(SWITCH_THEME_KEY, themeFromDevice())
}