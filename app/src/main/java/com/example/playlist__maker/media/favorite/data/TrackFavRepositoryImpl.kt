package com.example.playlist__maker.media.favorite.data

import com.example.playlist__maker.R
import com.example.playlist__maker.db.AppDatabase
import com.example.playlist__maker.db.tracks.FavoriteManager
import com.example.playlist__maker.db.tracks.MediaFavDbConvertor
import com.example.playlist__maker.db.tracks.MediaFavEntity
import com.example.playlist__maker.media.favorite.domain.TrackFavRepository
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.utils.ResponseCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TrackFavRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: MediaFavDbConvertor
) : TrackFavRepository {

    override suspend fun addToFavorite(track: Track) {
        withContext(Dispatchers.IO) {
            appDatabase.mediaFavDao().addTrackInFav(convertor.map(track))
            FavoriteManager.notifyFavoriteChanged(track.trackId, true)
        }
    }

    override suspend fun deleteFromFavorites(track: Track) {
        withContext(Dispatchers.IO) {
            appDatabase.mediaFavDao().deleteTrackInFav(convertor.map(track))
            FavoriteManager.notifyFavoriteChanged(track.trackId, false)
        }
    }

    override fun getFavTracks(): Flow<ResponseCode<List<Track>>> = flow {
        val tracks = withContext(Dispatchers.IO) {
            appDatabase.mediaFavDao().getTrackFav().reversed()
        }

        if (tracks.isEmpty())
            emit(ResponseCode.ClientError(R.string.nothing_found))
        else
            emit(ResponseCode.Success(convertListToList(tracks), status = 200))
    }

    override suspend fun isTrackFavorite(trackId: String): Boolean {
        return withContext(Dispatchers.IO) {
            appDatabase.mediaFavDao().getTrackById(trackId) != null
        }
    }

    private fun convertListToList(tracks: List<MediaFavEntity>)
            : List<Track> {
        return tracks.map { track -> convertor.map(track)}
    }

    override suspend fun getFavTrackIds(): List<String> {
        return withContext(Dispatchers.IO) {
            appDatabase.mediaFavDao().getFavTrackId()
        }
    }
}