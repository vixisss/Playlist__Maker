package com.example.playlist__maker.di

import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import com.example.playlist__maker.player.data.PlayerNetwork
import com.example.playlist__maker.search.data.NetworkClient
import com.example.playlist__maker.search.data.dto.History
import com.example.playlist__maker.search.data.dto.ItunesAPI
import com.example.playlist__maker.search.data.dto.RetrofitBuild
import com.example.playlist__maker.search.data.impl.TracksRepositoryImpl
import com.example.playlist__maker.utils.App
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single { PlayerNetwork() }
    single { History(get()) }
    single { TracksRepositoryImpl(get()) }

    single<ItunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesAPI::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(App.APP_PREFERENCES, MODE_PRIVATE)
    }

    single<NetworkClient> {
        RetrofitBuild(get())
    }


    factory { Gson() }
    factory { MediaPlayer() }
}