package com.example.playlist__maker.media.playlist.data

import com.example.playlist__maker.db.AppDatabase
import com.example.playlist__maker.db.playlists.PlaylistDbConvertor
import com.example.playlist__maker.db.playlists.PlaylistEntity
import com.example.playlist__maker.db.tracks.MediaFavDbConvertor
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import com.example.playlist__maker.media.playlist.domain.repository.PlaylistRepository
import com.example.playlist__maker.search.domain.models.Track
import com.google.gson.Gson

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertor: PlaylistDbConvertor,
    private val gson: Gson
) : PlaylistRepository {
    override suspend fun update(playlist: PlaylistEntity) {
        val currentTracks = getPlaylistTracks(playlist.id)
        appDatabase.playlistDao().update(
            playlist.copy(
                tracksJson = gson.toJson(currentTracks),
                tracksCount = currentTracks.size
            )
        )
    }

    override suspend fun getAllPlaylists(): List<PlaylistEntity> {
        return appDatabase.playlistDao().getAllPlaylists().sortedByDescending { it.id }
    }

    override suspend fun getPlaylistById(playlistId: Long): PlaylistEntity? {
        return appDatabase.playlistDao().getPlaylistById(playlistId)
    }

    override suspend fun delete(playlistId: Long) {
        appDatabase.playlistDao().delete(playlistId)
    }

    override suspend fun createPlaylist(playlist: Playlist): Long {
        val entity = dbConvertor.map(playlist)
        return appDatabase.playlistDao().insert(entity)
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track) {
        val playlistEntity = appDatabase.playlistDao().getPlaylistById(playlistId)
            ?: throw Exception("Плейлист не найден")

        val currentTracks = if (playlistEntity.tracksJson.isEmpty()) {
            emptyList()
        } else {
            Gson().fromJson(playlistEntity.tracksJson, Array<Track>::class.java).toList()
        }

        if (currentTracks.any { it.trackId == track.trackId }) {
            throw Exception("Трек уже есть в плейлисте")
        }

        val updatedTracks = currentTracks + track
        val updatedJson = Gson().toJson(updatedTracks)

        appDatabase.playlistDao().updatePlaylistTracks(
            playlistId,
            updatedJson,
            updatedTracks.size
        )
    }

    override suspend fun getPlaylistTracks(playlistId: Long): List<Track> {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        return if (playlist?.tracksJson?.isNotEmpty() == true) {
            gson.fromJson(playlist.tracksJson, Array<Track>::class.java).toList()
        } else {
            emptyList()
        }
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: String) {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId) ?: return

        val currentTracks = if (playlist.tracksJson.isNotEmpty()) {
            gson.fromJson(playlist.tracksJson, Array<Track>::class.java).toList()
        } else {
            emptyList()
        }

        val updatedTracks = currentTracks.filter { it.trackId != trackId }
        val updatedJson = gson.toJson(updatedTracks)

        appDatabase.playlistDao().updatePlaylistTracks(
            playlistId,
            updatedJson,
            updatedTracks.size
        )

        appDatabase.playlistDao().update(
            playlist.copy(
                tracksJson = updatedJson,
                tracksCount = updatedTracks.size
            )
        )
    }
}