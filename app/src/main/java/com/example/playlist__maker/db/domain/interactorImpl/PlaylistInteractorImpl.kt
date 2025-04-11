package com.example.playlist__maker.db.domain.interactorImpl

import com.example.playlist__maker.db.data.playlists.PlaylistEntity
import com.example.playlist__maker.db.domain.interactor.PlaylistInteractor
import com.example.playlist__maker.db.domain.models.Playlist
import com.example.playlist__maker.db.domain.repository.PlaylistRepository
import com.example.playlist__maker.search.domain.models.Track

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {

    override suspend fun insert(playlist: PlaylistEntity): Long {
       return playlistRepository.insert(playlist)
    }

    override suspend fun update(playlist: PlaylistEntity) {
        playlistRepository.update(playlist)
    }

    override suspend fun getAllPlaylists(): List<PlaylistEntity> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun getPlaylistById(playlistId: Long): PlaylistEntity? {
        return playlistRepository.getPlaylistById(playlistId)
    }

    override suspend fun delete(playlist: PlaylistEntity) {
        playlistRepository.delete(playlist)
    }

    override suspend fun createPlaylist(playlist: Playlist): Long {
        return playlistRepository.createPlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(
        playlistId: Long,
        track: Track
    ) {
        playlistRepository.addTrackToPlaylist(playlistId, track)
    }
}