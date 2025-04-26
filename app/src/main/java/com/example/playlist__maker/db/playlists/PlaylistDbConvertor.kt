package com.example.playlist__maker.db.playlists

import com.example.playlist__maker.media.playlist.domain.models.Playlist

class PlaylistDbConvertor {

    fun map(playlist: Playlist) : PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverPath = playlist.coverPath,
            tracksJson = "[]",
            tracksCount = playlist.tracksCount
        )
    }

    fun map(playlistEntity: PlaylistEntity) : Playlist {
        return Playlist(
            playlistEntity.id,
            playlistEntity.name,
            playlistEntity.description,
            playlistEntity.coverPath,
            playlistEntity.tracksJson,
            playlistEntity.tracksCount
        )
    }
}