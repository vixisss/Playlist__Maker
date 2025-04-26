package com.example.playlist__maker.media.playlist.domain.interactor

import com.example.playlist__maker.db.playlists.PlaylistEntity
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import com.example.playlist__maker.search.domain.models.Track

interface PlaylistInteractor {
    suspend fun getAllPlaylists(): List<PlaylistEntity>
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?
    suspend fun delete(playlistId: Long)
    suspend fun createPlaylist(playlist: Playlist): Long

    suspend fun addTrackToPlaylist(playlistId: Long, track: Track)

    suspend fun getPlaylistTracks(playlistId: Long): List<Track>
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: String)

    suspend fun updatePlaylist(playlist: Playlist)

}