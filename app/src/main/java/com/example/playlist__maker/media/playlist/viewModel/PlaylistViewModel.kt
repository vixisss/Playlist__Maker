package com.example.playlist__maker.media.playlist.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist__maker.db.data.playlists.PlaylistDbConvertor
import com.example.playlist__maker.db.domain.interactor.PlaylistInteractor
import com.example.playlist__maker.db.domain.models.Playlist
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.utils.PlaylistDateManager
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PlaylistViewModel(
    private val playlistRepository: PlaylistInteractor,
    private val context: Context

) : ViewModel() {

    private val _playlistCreated = MutableLiveData<Boolean>()

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    private val _playlistTracks = MutableLiveData<List<Track>>()
    val playlistTracks: LiveData<List<Track>> = _playlistTracks

    fun loadPlaylistTracks(playlistId: Long) {
        viewModelScope.launch {
            _playlistTracks.value = playlistRepository.getPlaylistTracks(playlistId)
        }
    }


    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            try {
                // Всегда сохраняем текущую дату создания
                val creationDate = System.currentTimeMillis()

                // Сохраняем обложку если есть
                val newCoverUri = playlist.coverPath?.let { uriString ->
                    savePlaylistCover(context, uriString.toUri(), playlist.id.toString())
                }

                // Создаем плейлист с датой создания
                val updatedPlaylist = playlist.copy(
                    coverPath = newCoverUri?.toString(),
                    creationDate = creationDate // Добавляем дату в модель
                )

                playlistRepository.createPlaylist(updatedPlaylist)
                _playlistCreated.value = true
                loadPlaylists()
            } catch (e: Exception) {
                _playlistCreated.value = false
            }
        }
    }

    private fun savePlaylistCover(context: Context, uri: Uri, playlistId: String): Uri {
        val storageDir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playlist_covers"
        ).apply { if (!exists()) mkdirs() }

        val fileName = "cover_${playlistId}.jpg"
        val file = File(storageDir, fileName)

        return try {
            if (uri != Uri.EMPTY) {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)?.let { bitmap ->
                        FileOutputStream(file).use { outputStream ->
                            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)) {
                                throw IOException("Compression failed")
                            }
                        }
                        return Uri.fromFile(file)
                    } ?: throw NullPointerException("Failed to decode bitmap")
                } ?: throw IllegalStateException("Cannot open input stream")
            }
            Uri.fromFile(file)
        } catch (e: Exception) {
            Uri.fromFile(file)
        }
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            try {
                val playlistEntities = playlistRepository.getAllPlaylists()
                val playlists = playlistEntities.map { playlistEntity ->
                    PlaylistDbConvertor().map(playlistEntity)
                }
                _playlists.value = playlists
            } catch (e: Exception) {
            }
        }
    }

    fun addTrackToPlaylist(playlistId: Long, track: Track) {
        viewModelScope.launch {
            try {
                playlistRepository.addTrackToPlaylist(playlistId, track)
            } catch (e: Exception) {
            }
        }
    }


    fun removeTrackFromPlaylist(playlistId: Long, trackId: String) {
        viewModelScope.launch {
            playlistRepository.removeTrackFromPlaylist(playlistId, trackId)
            loadPlaylistTracks(playlistId) // Обновляем список после удаления
        }
    }
}