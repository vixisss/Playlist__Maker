package com.example.playlist__maker.settings.domain.interactor

interface AppSwitcherRepository {
    fun getTheme(): Boolean
    fun saveTheme(saveTheme: Boolean)
    fun switchTheme(switchTheme: Boolean)
}