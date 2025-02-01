package com.example.playlist__maker.settings.domain.interactor.impl

import com.example.playlist__maker.settings.domain.interactor.AppSwitcherInteractor
import com.example.playlist__maker.settings.domain.repository.AppSwitcherRepository

class AppSwitcherInteractorImpl(
    private val appSwitcher: AppSwitcherRepository
) : AppSwitcherInteractor {


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