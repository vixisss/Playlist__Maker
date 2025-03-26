package com.example.playlist__maker.db.data

import com.example.playlist__maker.search.domain.models.Track

class MediaFavDbConvertor {

    fun map(track: Track): MediaFavEntity {
        return MediaFavEntity(
            track.trackId,
            track.trackName,
            track.trackTime,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(mediaFavEntity: MediaFavEntity): Track {
        return Track(
            mediaFavEntity.trackId,
            mediaFavEntity.trackName,
            mediaFavEntity.trackTime,
            mediaFavEntity.artistName!!,
            mediaFavEntity.trackTimeMillis,
            mediaFavEntity.artworkUrl100,
            mediaFavEntity.collectionName,
            mediaFavEntity.releaseDate,
            mediaFavEntity.primaryGenreName,
            mediaFavEntity.country,
            mediaFavEntity.previewUrl
        )
    }
}
