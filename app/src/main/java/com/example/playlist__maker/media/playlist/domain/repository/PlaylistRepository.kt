package com.example.playlist__maker.media.playlist.domain.repository

import com.example.playlist__maker.db.playlists.PlaylistEntity
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import com.example.playlist__maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun update(playlist: PlaylistEntity)
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
    fun getPlaylistById(playlistId: Long): Flow<PlaylistEntity?>
    suspend fun delete(playlistId: Long)
    suspend fun createPlaylist(playlist: Playlist): Long
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track)
    fun getPlaylistTracks(playlistId: Long): Flow<List<Track>>
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: String)
}