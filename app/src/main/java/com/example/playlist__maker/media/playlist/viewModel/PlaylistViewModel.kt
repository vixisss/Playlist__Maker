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
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PlaylistViewModel(
    private val playlistRepository: PlaylistInteractor
) : ViewModel() {

    private val _playlistCreated = MutableLiveData<Boolean>()

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    private fun savePlaylistCover(context: Context, uri: Uri, playlistId: String): Uri? {
        return try {
            val storageDir = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "playlist_covers"
            )
            if (!storageDir.exists()) storageDir.mkdirs()

            val fileName = "cover_${playlistId}_${System.currentTimeMillis()}.jpg"
            val file = File(storageDir, fileName)

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    BitmapFactory.decodeStream(inputStream)?.compress(
                        Bitmap.CompressFormat.JPEG,
                        85,
                        outputStream
                    )
                }
            }
            Uri.fromFile(file)
        } catch (e: Exception) {
            null
        }
    }


    fun createPlaylist(context: Context, playlist: Playlist) {
        viewModelScope.launch {
            try {
                val newCoverUri = playlist.coverPath?.let { uriString ->
                    savePlaylistCover(context, uriString.toUri(), playlist.id.toString())
                }

                val updatedPlaylist = playlist.copy(
                    coverPath = newCoverUri?.toString()
                )
                playlistRepository.createPlaylist(updatedPlaylist)
                _playlistCreated.value = true
                loadPlaylists()
            } catch (e: Exception) {
                _playlistCreated.value = false
            }
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
}