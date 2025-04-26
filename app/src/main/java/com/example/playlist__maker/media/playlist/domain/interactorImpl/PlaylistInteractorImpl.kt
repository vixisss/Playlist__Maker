package com.example.playlist__maker.media.playlist.domain.interactorImpl

import com.example.playlist__maker.db.playlists.PlaylistDbConvertor
import com.example.playlist__maker.db.playlists.PlaylistEntity
import com.example.playlist__maker.media.playlist.domain.interactor.PlaylistInteractor
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import com.example.playlist__maker.media.playlist.domain.repository.PlaylistRepository
import com.example.playlist__maker.search.domain.models.Track

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {

    override suspend fun getAllPlaylists(): List<PlaylistEntity> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun getPlaylistById(playlistId: Long): PlaylistEntity? {
        return playlistRepository.getPlaylistById(playlistId)
    }

    override suspend fun delete(playlistId: Long) {
        playlistRepository.delete(playlistId)
    }

    override suspend fun createPlaylist(playlist: Playlist): Long {
        return playlistRepository.createPlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track) {
        playlistRepository.addTrackToPlaylist(playlistId, track)
        val updatedTracks = playlistRepository.getPlaylistTracks(playlistId)
        val playlist = playlistRepository.getPlaylistById(playlistId) ?: return
        playlistRepository.update(playlist.copy(tracksCount = updatedTracks.size))
    }

    override suspend fun getPlaylistTracks(playlistId: Long): List<Track> {
        return playlistRepository.getPlaylistTracks(playlistId)
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: String) {
        playlistRepository.removeTrackFromPlaylist(playlistId, trackId)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val currentTracks = playlistRepository.getPlaylistTracks(playlist.id)
        val updatedPlaylist = playlist.copy(
            tracksCount = currentTracks.size
        )

        val entity = PlaylistDbConvertor().map(updatedPlaylist)
        playlistRepository.update(entity)
    }
}