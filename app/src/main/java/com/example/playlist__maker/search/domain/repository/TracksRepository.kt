package com.example.playlist__maker.search.domain.repository

import com.example.playlist__maker.utils.ResponseCode
import com.example.playlist__maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<ResponseCode<List<Track>>>
}