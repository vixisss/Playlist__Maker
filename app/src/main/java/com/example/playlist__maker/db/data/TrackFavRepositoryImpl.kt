package com.example.playlist__maker.db.data

import com.example.playlist__maker.db.domain.repository.TrackFavRepository
import com.example.playlist__maker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TrackFavRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: MediaFavDbConvertor
) : TrackFavRepository {

    override suspend fun addToFavorite(track: Track) = appDatabase.mediaFavDao().addTrackInFav(convertor.map(track))

    override suspend fun deleteFromFavorites(track: Track) {
        appDatabase.mediaFavDao().deleteTrackInFav(convertor.map(track))
    }

    override fun getFavTracks(): Flow<List<Track>> = flow {
        var tracks: List<Track>
        withContext(Dispatchers.IO) {
            val tracksEntity = appDatabase.mediaFavDao().getTrackFav()
            tracks = convertFromTrackEntity(tracksEntity)
            tracks.forEach {
                it.isFavorite = true
            }
        }
        emit(tracks)
    }

    private fun convertFromTrackEntity(tracks: List<MediaFavEntity>): List<Track> {
        return tracks.map { track -> convertor.map(track) }
    }
}