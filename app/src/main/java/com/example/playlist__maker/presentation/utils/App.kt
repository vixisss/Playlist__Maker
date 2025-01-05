package com.example.playlist__maker.presentation.utils

import android.app.Application
import android.content.SharedPreferences
import com.example.playlist__maker.domain.api.AppSwitcherInteractor

private const val EXAMPLE_PREFERENCES = "shared_preferences"

class App : Application() {

    private lateinit var preferencesShared: SharedPreferences
    private lateinit var appSwitcherInteractor: AppSwitcherInteractor


    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)

        preferencesShared = getSharedPreferences(EXAMPLE_PREFERENCES, MODE_PRIVATE)
        appSwitcherInteractor = Creator.provideAppSwitcherInteractor()
        appSwitcherInteractor.switchTheme(appSwitcherInteractor.getTheme())

    }

    fun getSharedPreferences(): SharedPreferences = preferencesShared
}