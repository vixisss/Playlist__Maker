package com.example.playlist__maker.db.domain.repository

import com.example.playlist__maker.db.data.playlists.PlaylistEntity
import com.example.playlist__maker.db.domain.models.Playlist

interface PlaylistRepository {
    suspend fun insert(playlist: PlaylistEntity): Long
    suspend fun update(playlist: PlaylistEntity)
    suspend fun getAllPlaylists(): List<PlaylistEntity>
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?
    suspend fun delete(playlist: PlaylistEntity)
    suspend fun createPlaylist(playlist: Playlist): Long
}