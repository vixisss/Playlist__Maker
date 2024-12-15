package com.example.playlist__maker.creator

import com.example.playlist__maker.data.network.RetrofitBuild
import com.example.playlist__maker.data.network.TracksRepositoryImpl
import com.example.playlist__maker.domain.api.TracksInteractor
import com.example.playlist__maker.domain.api.TracksRepository
import com.example.playlist__maker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitBuild())
    }

    fun provideMoviesInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}