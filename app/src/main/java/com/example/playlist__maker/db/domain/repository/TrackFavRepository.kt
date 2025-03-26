package com.example.playlist__maker.db.domain.repository

import com.example.playlist__maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackFavRepository {
    suspend fun addToFavorite(track: Track)
    suspend fun deleteFromFavorites(track: Track)
    fun getFavTracks(): Flow<List<Track>>
}