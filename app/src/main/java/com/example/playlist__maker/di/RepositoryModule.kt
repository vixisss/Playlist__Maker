package com.example.playlist__maker.di

import com.example.playlist__maker.db.playlists.PlaylistDbConvertor
import com.example.playlist__maker.media.playlist.data.PlaylistRepositoryImpl
import com.example.playlist__maker.media.favorite.data.TrackFavRepositoryImpl
import com.example.playlist__maker.db.tracks.MediaFavDbConvertor
import com.example.playlist__maker.media.playlist.domain.repository.PlaylistRepository
import com.example.playlist__maker.media.favorite.domain.TrackFavRepository
import com.example.playlist__maker.search.domain.repository.TracksRepository
import com.example.playlist__maker.search.domain.repository.TracksRepositoryImpl
import com.example.playlist__maker.settings.data.AppSwitcher
import com.example.playlist__maker.settings.domain.repository.AppSwitcherRepository
import com.example.playlist__maker.sharing.data.ExternalNavigatorImpl
import com.example.playlist__maker.sharing.domain.repository.ExternalNavigator
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single <TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single <AppSwitcherRepository> {
        AppSwitcher(androidApplication(), get())
    }

    single <ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    factory { MediaFavDbConvertor() }
    factory { PlaylistDbConvertor() }

    single<TrackFavRepository> {
        TrackFavRepositoryImpl(get(), get())
    }

    single <PlaylistRepository>{
        PlaylistRepositoryImpl(get(), get(), get())
    }
}