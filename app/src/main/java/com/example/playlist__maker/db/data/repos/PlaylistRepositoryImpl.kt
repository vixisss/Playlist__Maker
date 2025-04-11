package com.example.playlist__maker.db.data.repos

import android.util.Log
import com.example.playlist__maker.db.data.AppDatabase
import com.example.playlist__maker.db.data.playlists.PlaylistDbConvertor
import com.example.playlist__maker.db.data.playlists.PlaylistEntity
import com.example.playlist__maker.db.data.tracks.MediaFavDbConvertor
import com.example.playlist__maker.db.domain.models.Playlist
import com.example.playlist__maker.db.domain.repository.PlaylistRepository
import com.example.playlist__maker.search.domain.models.Track
import com.google.gson.Gson
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

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track) {
        withContext(Dispatchers.IO) {
            // Получаем текущий плейлист
            val playlistEntity = appDatabase.playlistDao().getPlaylistById(playlistId)
                ?: throw Exception("Плейлист не найден")

            // Десериализуем текущие треки
            val currentTracks = if (playlistEntity.tracksJson.isNullOrEmpty()) {
                emptyList()
            } else {
                Gson().fromJson(playlistEntity.tracksJson, Array<Track>::class.java).toList()
            }

            // Проверяем наличие трека
            if (currentTracks.any { it.trackId == track.trackId }) {
                throw Exception("Трек уже есть в плейлисте")
            }

            // Добавляем трек
            val updatedTracks = currentTracks + track
            val updatedJson = Gson().toJson(updatedTracks)

            // Обновляем плейлист
            appDatabase.playlistDao().updatePlaylistTracks(
                playlistId,
                updatedJson,
                updatedTracks.size
            )

            // Сохраняем трек в базу (если его там еще нет)
            try {
                appDatabase.mediaFavDao().addTrackInFav(MediaFavDbConvertor().map(track))
            } catch (e: Exception) {
                // Трек уже существует, игнорируем
            }
        }
    }


}