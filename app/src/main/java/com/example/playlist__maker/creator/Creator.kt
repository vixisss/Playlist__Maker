package com.example.playlist__maker.creator

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.playlist__maker.search.data.History
import com.example.playlist__maker.player.data.PlayerNetwork
import com.example.playlist__maker.dto.RetrofitBuild
import com.example.playlist__maker.search.data.impl.TracksRepositoryImpl
import com.example.playlist__maker.player.domain.interactors.PlayerInteractor
import com.example.playlist__maker.search.domain.HistoryInteractor
import com.example.playlist__maker.search.domain.TracksInteractor
import com.example.playlist__maker.search.domain.TracksRepository
import com.example.playlist__maker.player.domain.impl.PlayerInteractorImpl
import com.example.playlist__maker.search.domain.impl.HistoryInteractorImpl
import com.example.playlist__maker.search.domain.impl.TracksInteractorImpl
import com.example.playlist__maker.settings.data.AppSwitcher
import com.example.playlist__maker.settings.domain.impl.AppSwitcherInteractorImpl
import com.example.playlist__maker.sharing.domain.SharingInteractor
import com.example.playlist__maker.settings.domain.interactor.AppSwitcherInteractor
import com.example.playlist__maker.sharing.data.ExternalNavigatorImpl
import com.example.playlist__maker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlist__maker.utils.App

object Creator {
    private lateinit var application: Application

    private fun provideTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            networkClient = RetrofitBuild()
        )
    }

    fun initApplication(application: App) {
        Creator.application = application
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
                preferencesShared = application.getSharedPreferences(App.APP_PREFERENCES, MODE_PRIVATE)
            )
        )
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(
            externalNavigator = ExternalNavigatorImpl(
                context = application
            )
        )
    }


    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(history = History(
            sharedPreferences = (application as App).getSharedPreferences(App.APP_PREFERENCES, MODE_PRIVATE)
        )
        )
    }
}