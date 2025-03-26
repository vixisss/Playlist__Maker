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
        const val SUPER = 200
    }

    override fun searchTracks(expression: String): Flow<ResponseCode<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        // Инициализируем tracksList пустым списком
        var tracksList: List<Track> = emptyList()

        if (response is TrackSearchResponse) {
            // Фильтруем и преобразуем данные
            tracksList = response.results.filter {
                it.trackName.isNotEmpty() &&
                        it.artistName.isNotEmpty() &&
                        it.trackTimeMillis > 0
            }.map {
                Track(
                    trackId = it.trackId,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTime = it.trackTime,
                    trackTimeMillis = it.trackTimeMillis,
                    artworkUrl100 = it.artworkUrl100,
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    previewUrl = it.previewUrl
                )
            }

            // Отправляем результат в Flow
            if (tracksList.isEmpty()) {
                emit(
                    ResponseCode.ClientError(
                        status = NOTHING_FOUND
                    )
                )
            } else {
                emit(
                    ResponseCode.Success(
                        data = tracksList,
                        status = SUPER
                    )
                )
            }
        } else {
            // Если ответ не успешный, отправляем ошибку
            emit(
                ResponseCode.ClientError(
                    status = NO_INTERNET
                )
            )
        }

        withContext(Dispatchers.IO) {
            val allIds = appDatabase.mediaFavDao().getFavTrackId()

            tracksList.forEach { track ->
                if (track.trackId in allIds) {
                    track.isFavorite = true
                }
            }
        }
    }
}