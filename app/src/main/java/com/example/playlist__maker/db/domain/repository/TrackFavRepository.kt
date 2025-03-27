package com.example.playlist__maker.db.domain.repository

import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.utils.ResponseCode
import kotlinx.coroutines.flow.Flow

interface TrackFavRepository {
    suspend fun addToFavorite(track: Track)
    suspend fun deleteFromFavorites(track: Track)
    fun getFavTracks(): Flow<ResponseCode<List<Track>>>
    suspend fun isTrackFavorite(trackId: String): Boolean
    suspend fun getFavTrackIds(): List<String>
}
