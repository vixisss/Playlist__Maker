package com.example.playlist__maker.media.playlist.ui.viewModel

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
import com.example.playlist__maker.db.playlists.PlaylistDbConvertor
import com.example.playlist__maker.media.playlist.domain.interactor.PlaylistInteractor
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import com.example.playlist__maker.search.domain.models.Track
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

open class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val context: Context
) : ViewModel() {


    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    private val _playlistTracks = MutableLiveData<List<Track>>()
    val playlistTracks: LiveData<List<Track>> = _playlistTracks


    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            try {
                playlistInteractor.deletePlaylist(playlistId)
                loadPlaylists()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to delete playlist"
            } finally {
            }
        }
    }

    fun loadPlaylistTracks(playlistId: Long) {
        viewModelScope.launch {
            try {
                playlistInteractor.getPlaylistTracks(playlistId).collectLatest { tracks ->
                    val sortedTracks = tracks.sortedByDescending { it.addedDate ?: 0 }
                    _playlistTracks.value = sortedTracks
                }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load playlist tracks"
            } finally {
            }
        }
    }

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            try {
                val creationDate = System.currentTimeMillis()

                val newCoverUri = playlist.coverPath?.let { uriString ->
                    savePlaylistCover(context, uriString.toUri(), playlist.id.toString())
                }

                val updatedPlaylist = playlist.copy(
                    coverPath = newCoverUri?.toString(),
                    creationDate = creationDate
                )

                playlistInteractor.createPlaylist(updatedPlaylist)
                loadPlaylists()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to create playlist"
            } finally {
            }
        }
    }

    protected fun savePlaylistCover(context: Context, uri: Uri, playlistId: String): Uri {
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
                playlistInteractor.getAllPlaylists().collectLatest { playlists ->
                    _playlists.value = playlists
                }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load playlists"
            } finally {
            }
        }
    }

    fun addTrackToPlaylist(playlistId: Long, track: Track) {
        viewModelScope.launch {
            try {
                val trackWithDate = track.copy(addedDate = System.currentTimeMillis())
                playlistInteractor.addTrackToPlaylist(playlistId, trackWithDate)
                loadPlaylists()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to add track to playlist"
            } finally {
            }
        }
    }

    fun removeTrackFromPlaylist(playlistId: Long, trackId: String) {
        viewModelScope.launch {
            try {
                playlistInteractor.removeTrackFromPlaylist(playlistId, trackId)
                loadPlaylistTracks(playlistId)
                loadPlaylists()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to remove track from playlist"
            } finally {
            }
        }
    }
}