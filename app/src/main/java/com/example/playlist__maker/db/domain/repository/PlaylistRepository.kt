package com.example.playlist__maker.db.domain.repository

import com.example.playlist__maker.db.data.playlists.PlaylistEntity
import com.example.playlist__maker.db.domain.models.Playlist
import com.example.playlist__maker.search.domain.models.Track

interface PlaylistRepository {
    suspend fun insert(playlist: PlaylistEntity): Long
    suspend fun update(playlist: PlaylistEntity)
    suspend fun getAllPlaylists(): List<PlaylistEntity>
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?
    suspend fun delete(playlist: PlaylistEntity)
    suspend fun createPlaylist(playlist: Playlist): Long

    suspend fun addTrackToPlaylist(playlistId: Long, track: Track)

    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: String)
    suspend fun getPlaylistTracks(playlistId: Long): List<Track>
}