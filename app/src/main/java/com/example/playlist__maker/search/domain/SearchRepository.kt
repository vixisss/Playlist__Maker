package com.example.playlist__maker.search.domain

import com.example.playlist__maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTracks(query: String): Flow<List<Track>>
    fun addTrackToHistory(track: Track)
    fun showHistoryList(): List<Track>
    fun clearHistory()
}
