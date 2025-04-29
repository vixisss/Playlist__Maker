package com.example.playlist__maker.media.favorite.domain.impl

import com.example.playlist__maker.media.favorite.domain.TrackFavRepository
import com.example.playlist__maker.media.favorite.domain.TrackFavInteractor
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.utils.ResponseCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackFavInteractorImpl(
    private val trackFavRepository: TrackFavRepository
) : TrackFavInteractor {

    override suspend fun addToFavorite(track: Track) {
        trackFavRepository.addToFavorite(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        trackFavRepository.deleteFromFavorites(track)
    }

    override fun getFavTracks(): Flow<Pair<List<Track>?, String?>> {
        return trackFavRepository.getFavTracks().map { result ->
            when (result) {
                is ResponseCode.Success -> {
                    Pair(result.data, null)
                }
                is ResponseCode.ClientError -> {
                    Pair(null, "Client error: ${result.status}")
                }
                is ResponseCode.ServerError -> {
                    Pair(null, "Server error: ${result.status}")
                }
            }
        }
    }

    override suspend fun isTrackFavorite(trackId: String): Boolean {
        return trackFavRepository.isTrackFavorite(trackId)
    }
}