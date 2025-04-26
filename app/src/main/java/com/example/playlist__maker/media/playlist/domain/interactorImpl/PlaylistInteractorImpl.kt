package com.example.playlist__maker.media.playlist.domain.interactorImpl

import com.example.playlist__maker.db.playlists.PlaylistDbConvertor
import com.example.playlist__maker.db.playlists.PlaylistEntity
import com.example.playlist__maker.media.playlist.domain.interactor.PlaylistInteractor
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import com.example.playlist__maker.media.playlist.domain.repository.PlaylistRepository
import com.example.playlist__maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val dbConvertor: PlaylistDbConvertor
) : PlaylistInteractor {

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.update(dbConvertor.map(playlist))
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylists()
            .map { entities -> entities.map { dbConvertor.map(it) } }
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist?> {
        return playlistRepository.getPlaylistById(playlistId)
            .map { entity -> entity?.let { dbConvertor.map(it) } }
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        playlistRepository.delete(playlistId)
    }

    override suspend fun createPlaylist(playlist: Playlist): Long {
        return playlistRepository.createPlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track) {
        playlistRepository.addTrackToPlaylist(playlistId, track)
    }

    override fun getPlaylistTracks(playlistId: Long): Flow<List<Track>> {
        return playlistRepository.getPlaylistTracks(playlistId)
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: String) {
        playlistRepository.removeTrackFromPlaylist(playlistId, trackId)
    }
}