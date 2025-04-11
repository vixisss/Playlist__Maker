package com.example.playlist__maker.media.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.playlist__maker.db.data.playlists.PlaylistDbConvertor
import com.example.playlist__maker.db.domain.models.Playlist
import com.example.playlist__maker.db.domain.repository.PlaylistRepository
import com.example.playlist__maker.player.ui.viewModel.PlayerViewModel.PlayerUiState
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.utils.PlayState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _playlistCreated = MutableLiveData<Boolean>()

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