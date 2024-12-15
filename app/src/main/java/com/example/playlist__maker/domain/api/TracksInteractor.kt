package com.example.playlist__maker.domain.api

import com.example.playlist__maker.domain.models.Track


interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundMovies: List<Track>)
    }
}