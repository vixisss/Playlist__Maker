package com.example.playlist__maker.db.data.repos

import android.util.Log
import com.example.playlist__maker.db.data.AppDatabase
import com.example.playlist__maker.db.data.playlists.PlaylistDbConvertor
import com.example.playlist__maker.db.data.playlists.PlaylistEntity
import com.example.playlist__maker.db.domain.models.Playlist
import com.example.playlist__maker.db.domain.repository.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertor: PlaylistDbConvertor
) : PlaylistRepository {

    override suspend fun insert(playlist: PlaylistEntity): Long {
        return withContext(Dispatchers.IO) {
            appDatabase.playlistDao().insert(playlist)
        }
    }

    override suspend fun update(playlist: PlaylistEntity) {
        withContext(Dispatchers.IO) {
            appDatabase.playlistDao().update(playlist)
        }
    }

    override suspend fun getAllPlaylists(): List<PlaylistEntity> {
        return withContext(Dispatchers.IO) {
            appDatabase.playlistDao().getAllPlaylists().sortedByDescending { it.id }
        }
    }

    override suspend fun getPlaylistById(playlistId: Long): PlaylistEntity? {
        return withContext(Dispatchers.IO) {
            appDatabase.playlistDao().getPlaylistById(playlistId)
        }
    }

    override suspend fun delete(playlist: PlaylistEntity) {
        withContext(Dispatchers.IO) {
            appDatabase.playlistDao().delete(playlist)
        }
    }

    override suspend fun createPlaylist(playlist: Playlist): Long {
        return withContext(Dispatchers.IO) {
            val entity = dbConvertor.map(playlist)
            appDatabase.playlistDao().insert(entity)
            Log.d("create", "CREATE").toLong()
        }
    }
}