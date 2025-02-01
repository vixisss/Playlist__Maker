package com.example.playlist__maker.search.domain.impl

import com.example.playlist__maker.search.domain.TracksInteractor
import com.example.playlist__maker.search.domain.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}