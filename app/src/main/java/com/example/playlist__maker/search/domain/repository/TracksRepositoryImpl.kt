package com.example.playlist__maker.search.domain.repository

import com.example.playlist__maker.search.data.NetworkClient
import com.example.playlist__maker.search.data.TrackSearchRequest
import com.example.playlist__maker.search.data.TrackSearchResponse
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.utils.ResponseCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl (
    private val networkClient: NetworkClient
) : TracksRepository {

    companion object{
        const val NOTHING_FOUND = 404
        const val NO_INTERNET = 500
        const val SUPER = 200
    }


    override fun searchTracks(expression: String): Flow<ResponseCode<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        if(response is TrackSearchResponse){
            val tracksList = response.results.filter {
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
            if (tracksList.isEmpty()) {
                emit(
                    value = ResponseCode.ClientError(
                        status = NOTHING_FOUND
                    )
                )
            } else {
                emit(
                    value = ResponseCode.Success(
                        data = tracksList,
                        status = SUPER
                    )
                )

            }
        } else {
            emit(
                value = ResponseCode.ClientError(
                    status = NO_INTERNET
                )
            )
        }
    }
}