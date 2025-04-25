package com.example.playlist__maker.di

import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlist__maker.db.AppDatabase
import com.example.playlist__maker.player.data.PlayerNetwork
import com.example.playlist__maker.search.data.NetworkClient
import com.example.playlist__maker.search.data.dto.History
import com.example.playlist__maker.search.data.dto.ItunesAPI
import com.example.playlist__maker.search.data.dto.RetrofitBuild
import com.example.playlist__maker.search.domain.repository.TracksRepositoryImpl
import com.example.playlist__maker.utils.App
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single { PlayerNetwork(mediaPlayer = null) }
    single { History(get(), get()) }
    single { TracksRepositoryImpl(get(), get()) }

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

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }


    factory { Gson() }
    factory { MediaPlayer() }
}