package com.example.playlist__maker.presentation.utils

import android.app.Application
import com.example.playlist__maker.data.network.AppSwitcher
import com.example.playlist__maker.data.network.History
import com.example.playlist__maker.data.network.PlayerNetwork
import com.example.playlist__maker.data.network.RetrofitBuild
import com.example.playlist__maker.data.network.TracksRepositoryImpl
import com.example.playlist__maker.domain.api.AppSwitcherInteractor
import com.example.playlist__maker.domain.api.PlayerInteractor
import com.example.playlist__maker.domain.api.HistoryInteractor
import com.example.playlist__maker.domain.api.TracksInteractor
import com.example.playlist__maker.domain.api.TracksRepository
import com.example.playlist__maker.domain.impl.AppSwitcherInteractorImpl
import com.example.playlist__maker.domain.impl.PlayerInteractorImpl
import com.example.playlist__maker.domain.impl.HistoryInteractorImpl
import com.example.playlist__maker.domain.impl.TracksInteractorImpl

object Creator {
    private lateinit var application: Application

    private fun provideTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            networkClient = RetrofitBuild()
        )
    }

    fun initApplication(application: Application) {
        this.application = application
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(
            repository = provideTracksRepository()
        )
    }

    fun providePlayer(): PlayerInteractor {
        return PlayerInteractorImpl(PlayerNetwork())
    }

    fun provideAppSwitcherInteractor(): AppSwitcherInteractor {
        return AppSwitcherInteractorImpl(
            AppSwitcher(
                app = application,
                preferencesShared = (application as App).getSharedPreferences()
            )
        )
    }

    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(history = History(
            sharedPreferences = (application as App).getSharedPreferences()
        ))
    }
}