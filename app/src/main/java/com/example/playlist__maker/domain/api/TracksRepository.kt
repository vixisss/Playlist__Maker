package com.example.playlist__maker.domain.api

import com.example.playlist__maker.domain.models.ResponseCode
import com.example.playlist__maker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): ResponseCode<List<Track>>
}