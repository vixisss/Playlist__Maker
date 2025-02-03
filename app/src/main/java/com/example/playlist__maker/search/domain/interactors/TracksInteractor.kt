package com.example.playlist__maker.search.domain.interactors

import com.example.playlist__maker.utils.ResponseCode
import com.example.playlist__maker.search.domain.models.Track


interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrack: ResponseCode<List<Track>>)
    }
}