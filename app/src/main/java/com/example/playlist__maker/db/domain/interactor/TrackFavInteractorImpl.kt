package com.example.playlist__maker.db.domain.interactor

import com.example.playlist__maker.db.domain.repository.TrackFavRepository
import com.example.playlist__maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class TrackFavInteractorImpl(
    private val trackFavRepository: TrackFavRepository
) : TrackFavInteractor {

    override suspend fun addToFavorite(track: Track) {
        trackFavRepository.addToFavorite(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        trackFavRepository.deleteFromFavorites(track)
    }

    override fun getFavTracks(): Flow<List<Track>> {
        return trackFavRepository.getFavTracks()
    }
}