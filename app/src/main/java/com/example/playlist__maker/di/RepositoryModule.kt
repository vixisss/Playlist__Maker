package com.example.playlist__maker.di

import com.example.playlist__maker.search.domain.repository.TracksRepository
import com.example.playlist__maker.search.data.impl.TracksRepositoryImpl
import com.example.playlist__maker.settings.data.AppSwitcher
import com.example.playlist__maker.settings.domain.repository.AppSwitcherRepository
import com.example.playlist__maker.sharing.data.ExternalNavigatorImpl
import com.example.playlist__maker.sharing.domain.repository.ExternalNavigator
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single <TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single <AppSwitcherRepository> {
        AppSwitcher(androidApplication(), get())
    }

    single <ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }
}