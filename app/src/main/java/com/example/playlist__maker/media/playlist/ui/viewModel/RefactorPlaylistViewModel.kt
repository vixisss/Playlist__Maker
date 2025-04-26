package com.example.playlist__maker.media.playlist.ui.viewModel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlist__maker.media.playlist.domain.interactor.PlaylistInteractor
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import kotlinx.coroutines.launch

class RefactorPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val context: Context
) : PlaylistViewModel(playlistInteractor, context) {

     fun updatePlaylist(playlist: Playlist): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            try {
                val newCoverUri = playlist.coverPath?.let { uriString ->
                    if (uriString != playlist.coverPath) {
                        savePlaylistCover(context, uriString.toUri(), playlist.id.toString())
                    } else {
                        uriString.toUri()
                    }
                }

                val updatedPlaylist = playlist.copy(
                    coverPath = newCoverUri?.toString()
                )

                playlistInteractor.updatePlaylist(updatedPlaylist)
                loadPlaylists()
                result.postValue(true)
            } catch (e: Exception) {
                result.postValue(false)
            }
        }
        return result
    }
}