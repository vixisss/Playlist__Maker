package com.example.playlist__maker.domain.api

import com.example.playlist__maker.domain.models.ResponseCode
import com.example.playlist__maker.domain.models.Track


interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrack: ResponseCode<List<Track>> )
    }
}