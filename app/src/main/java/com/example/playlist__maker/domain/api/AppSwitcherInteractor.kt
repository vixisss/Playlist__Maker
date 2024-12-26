package com.example.playlist__maker.domain.api

interface AppSwitcherInteractor {
    fun getTheme(): Boolean
    fun saveTheme(saveTheme: Boolean)
    fun switchTheme(switchTheme: Boolean)
}