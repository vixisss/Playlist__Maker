package com.example.playlist__maker.settings.domain.impl

import com.example.playlist__maker.settings.data.AppSwitcher
import com.example.playlist__maker.settings.domain.interactor.AppSwitcherInteractor

class AppSwitcherInteractorImpl(private var appSwitcher: AppSwitcher) : AppSwitcherInteractor {


    override fun getTheme(): Boolean {
        return  appSwitcher.getTheme()
    }

    override fun switchTheme(switchTheme: Boolean) {
        appSwitcher.switchTheme(switchTheme)
    }

    override fun saveTheme(saveTheme: Boolean) {
        appSwitcher.saveTheme(saveTheme)
    }

}