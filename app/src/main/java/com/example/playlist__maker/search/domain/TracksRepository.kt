package com.example.playlist__maker.search.domain

import com.example.playlist__maker.utils.ResponseCode
import com.example.playlist__maker.search.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): ResponseCode<List<Track>>
}