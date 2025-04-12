package com.example.playlist__maker.search.domain.repository

import com.example.playlist__maker.db.data.AppDatabase
import com.example.playlist__maker.search.data.NetworkClient
import com.example.playlist__maker.search.data.TrackSearchRequest
import com.example.playlist__maker.search.data.TrackSearchResponse
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.utils.ResponseCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) : TracksRepository {

    companion object {
        const val NOTHING_FOUND = 404
        const val NO_INTERNET = 500
        const val SUCCESS = 200
    }

    override fun searchTracks(expression: String): Flow<ResponseCode<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when (response) {
            is TrackSearchResponse -> {
                val filteredTracks = response.results.filter {
                    it.trackName.isNotEmpty() &&
                            it.artistName.isNotEmpty() &&
                            it.trackTimeMillis > 0
                }

                if (filteredTracks.isEmpty()) {
                    emit(ResponseCode.ClientError(status = NOTHING_FOUND))
                    return@flow
                }

                val favoriteIds = withContext(Dispatchers.IO) {
                    appDatabase.mediaFavDao().getFavTrackId()
                }

                val tracksWithFavorites = filteredTracks.map { result ->
                    Track(
                        trackId = result.trackId,
                        trackName = result.trackName,
                        artistName = result.artistName,
                        trackTime = result.trackTime,
                        trackTimeMillis = result.trackTimeMillis,
                        artworkUrl100 = result.artworkUrl100,
                        collectionName = result.collectionName,
                        releaseDate = result.releaseDate,
                        primaryGenreName = result.primaryGenreName,
                        country = result.country,
                        previewUrl = result.previewUrl,
                        isFavorite = result.trackId in favoriteIds
                    )
                }

                emit(ResponseCode.Success(data = tracksWithFavorites, status = SUCCESS))
            }

            else -> {
                emit(ResponseCode.ClientError(status = NO_INTERNET))
            }
        }
    }
}