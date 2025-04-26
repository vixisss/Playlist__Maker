package com.example.playlist__maker.media.playlist.domain.interactor

import com.example.playlist__maker.db.playlists.PlaylistEntity
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import com.example.playlist__maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun updatePlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    fun getPlaylistById(playlistId: Long): Flow<Playlist?>
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun createPlaylist(playlist: Playlist): Long
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track)
    fun getPlaylistTracks(playlistId: Long): Flow<List<Track>>
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: String)
}