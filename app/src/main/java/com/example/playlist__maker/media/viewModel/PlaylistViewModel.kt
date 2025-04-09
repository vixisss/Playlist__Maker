package com.example.playlist__maker.media.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist__maker.db.data.AppDatabase
import com.example.playlist__maker.db.data.playlists.PlaylistDbConvertor
import com.example.playlist__maker.db.domain.models.Playlist
import com.example.playlist__maker.db.domain.repository.PlaylistRepository
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _playlistCreated = MutableLiveData<Boolean>()
    val playlistCreated: LiveData<Boolean> = _playlistCreated

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            try {
                playlistRepository.createPlaylist(playlist)
                _playlistCreated.value = true
                loadPlaylists() // Refresh the list after creation
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
                // Handle error
            }
        }
    }
}