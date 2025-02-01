package com.example.playlist__maker.settings.domain.repository

interface AppSwitcherRepository {
    fun getTheme(): Boolean
    fun saveTheme(saveTheme: Boolean)
    fun switchTheme(switchTheme: Boolean)
}