package com.example.playlist__maker.search.data.impl

import com.example.playlist__maker.search.data.NetworkClient
import com.example.playlist__maker.search.data.TrackSearchRequest
import com.example.playlist__maker.search.data.TrackSearchResponse
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.search.domain.TracksRepository
import com.example.playlist__maker.utils.ResponseCode


const val CLIENT_ERROR = 404
const val SERVER_ERROR = 500

class TracksRepositoryImpl (private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): ResponseCode<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return when (response.resultCode) {
            SERVER_ERROR -> {
                ResponseCode.ServerError(
                    status = SERVER_ERROR
                )
            }
            CLIENT_ERROR -> {
                ResponseCode.ClientError(
                    status = CLIENT_ERROR
                )
            }
            else -> {
                if (response is TrackSearchResponse) {
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
                        ResponseCode.ClientError(
                            status = 404
                        )
                    } else {
                        ResponseCode.Success(
                            data = tracksList
                        )
                    }
                } else {
                    ResponseCode.ClientError(
                        status = CLIENT_ERROR
                    )
                }
            }
        }
    }
}