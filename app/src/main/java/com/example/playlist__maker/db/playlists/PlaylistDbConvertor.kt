package com.example.playlist__maker.db.playlists

import com.example.playlist__maker.media.playlist.domain.models.Playlist

class PlaylistDbConvertor {

    fun map(playlist: Playlist) : PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverPath,
            playlist.tracksIdJson,
            playlist.tracksCount
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