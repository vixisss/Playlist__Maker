package com.example.playlist__maker.data.network

import com.example.playlist__maker.data.dto.TrackSearchRequest
import com.example.playlist__maker.data.dto.TrackSearchResponse
import com.example.playlist__maker.domain.models.Track
import com.example.playlist__maker.domain.api.TracksRepository
import com.example.playlist__maker.domain.models.ResponseCode

class TracksRepositoryImpl (private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): ResponseCode<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return if (response is TrackSearchResponse) {
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
            ResponseCode.Success(tracksList)
        } else {
            ResponseCode.Error("error")
        }
    }
}
