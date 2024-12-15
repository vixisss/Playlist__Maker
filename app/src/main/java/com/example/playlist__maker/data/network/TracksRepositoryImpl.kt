package com.example.playlist__maker.data.network

import com.example.playlist__maker.data.dto.TrackSearchRequest
import com.example.playlist__maker.data.dto.TrackSearchResponse
import com.example.playlist__maker.domain.models.Track
import com.example.playlist__maker.domain.api.TracksRepository

class TracksRepositoryImpl (private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return if (response.resultCode == 200) {
            (response as TrackSearchResponse).results.map { track ->
                Track(
                    track.trackId,
                    track.resultType,
                    track.trackName,
                    track.artistName,
                    track.trackTime,
                    track.trackTimeMillis,
                    track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
                    track.collectionName,
                    track.releaseDate,
                    track.primaryGenreName,
                    track.country ?: "",
                    track.previewUrl
                )
            }
        } else {
            emptyList()
        }
    }

}
