package com.example.playlist__maker.settings.data

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlist__maker.settings.domain.repository.AppSwitcherRepository

class AppSwitcher(
    private val app: Application,
    private val preferencesShared: SharedPreferences
): AppSwitcherRepository {
    companion object{
        private const val SWITCH_THEME_KEY = "key_of_switch_theme"
    }
    override fun switchTheme(switchTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (switchTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        saveTheme(switchTheme)
    }

    override fun saveTheme(saveTheme: Boolean) {
        preferencesShared.edit()
            .putBoolean(SWITCH_THEME_KEY, saveTheme)
            .apply()
    }

    private fun themeFromDevice(): Boolean = app.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    override fun getTheme(): Boolean = preferencesShared.getBoolean(SWITCH_THEME_KEY, themeFromDevice())
}