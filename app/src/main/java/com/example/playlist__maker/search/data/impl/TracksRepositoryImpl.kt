package com.example.playlist__maker.search.data.impl

import com.example.playlist__maker.search.data.NetworkClient
import com.example.playlist__maker.search.data.TrackSearchRequest
import com.example.playlist__maker.search.data.TrackSearchResponse
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.search.domain.TracksRepository
import com.example.playlist__maker.utils.ResponseCode

class TracksRepositoryImpl (private val networkClient: NetworkClient) : TracksRepository {

    companion object{
        const val NOTHING_FOUND = 404
        const val NO_INTERNET = 500
    }


    override fun searchTracks(expression: String): ResponseCode<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return when (response.resultCode) {
            NO_INTERNET -> {
                ResponseCode.ServerError(
                    status = NO_INTERNET
                )
            }
            NOTHING_FOUND -> {
                ResponseCode.ClientError(
                    status = NOTHING_FOUND
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
                        status = NOTHING_FOUND
                    )
                }
            }
        }
    }
}