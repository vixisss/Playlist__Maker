package com.example.playlist__maker.di

import com.example.playlist__maker.media.playlist.domain.interactor.PlaylistInteractor
import com.example.playlist__maker.media.favorite.domain.TrackFavInteractor
import com.example.playlist__maker.media.playlist.domain.interactorImpl.PlaylistInteractorImpl
import com.example.playlist__maker.media.favorite.domain.impl.TrackFavInteractorImpl
import com.example.playlist__maker.player.domain.impl.PlayerInteractorImpl
import com.example.playlist__maker.player.domain.interactors.PlayerInteractor
import com.example.playlist__maker.search.domain.interactors.HistoryInteractor
import com.example.playlist__maker.search.domain.interactors.TracksInteractor
import com.example.playlist__maker.search.domain.interactors.impl.HistoryInteractorImpl
import com.example.playlist__maker.search.domain.interactors.impl.TracksInteractorImpl
import com.example.playlist__maker.settings.domain.interactor.impl.AppSwitcherInteractorImpl
import com.example.playlist__maker.settings.domain.interactor.AppSwitcherInteractor
import com.example.playlist__maker.sharing.domain.interactor.SharingInteractor
import com.example.playlist__maker.sharing.domain.interactor.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single <PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    single <HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    single <TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single <AppSwitcherInteractor> {
        AppSwitcherInteractorImpl(get())
    }

    single <SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<TrackFavInteractor> {
        TrackFavInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}