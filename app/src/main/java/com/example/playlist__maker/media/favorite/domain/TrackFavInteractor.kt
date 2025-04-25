package com.example.playlist__maker.media.favorite.domain

import com.example.playlist__maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackFavInteractor {
    suspend fun addToFavorite(track: Track)
    suspend fun deleteFromFavorites(track: Track)
    fun getFavTracks(): Flow<Pair<List<Track>?, String?>>
    suspend fun isTrackFavorite(trackId: String): Boolean
}