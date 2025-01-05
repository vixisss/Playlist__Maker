package com.example.playlist__maker.domain.impl

import com.example.playlist__maker.data.network.AppSwitcher
import com.example.playlist__maker.domain.api.AppSwitcherInteractor

class AppSwitcherInteractorImpl(private val appSwitcher: AppSwitcher) : AppSwitcherInteractor{
    override fun getTheme(): Boolean = appSwitcher.getTheme()

    override fun saveTheme(saveTheme: Boolean) {
        appSwitcher.saveTheme(saveTheme)
    }

    override fun switchTheme(switchTheme: Boolean) {
        appSwitcher.switchTheme(switchTheme)
    }
}