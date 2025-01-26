package com.example.playlist__maker.settings.domain.interactor

interface AppSwitcherInteractor {
    fun getTheme(): Boolean
    fun saveTheme(saveTheme: Boolean)
    fun switchTheme(switchTheme: Boolean)
}