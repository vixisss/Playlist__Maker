package com.example.playlist__maker.utils

import android.app.Application
import com.example.playlist__maker.creator.Creator
import com.example.playlist__maker.settings.domain.interactor.AppSwitcherInteractor

class App : Application() {

    private lateinit var themeUseCase: AppSwitcherInteractor

    companion object {
        const val APP_PREFERENCES = "app_preferences"
    }

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        themeUseCase = Creator.provideAppSwitcherInteractor()
        themeUseCase.switchTheme(themeUseCase.getTheme())
    }
}