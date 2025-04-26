package com.example.playlist__maker.player.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist__maker.db.tracks.FavoriteManager
import com.example.playlist__maker.media.favorite.domain.TrackFavInteractor
import com.example.playlist__maker.player.domain.interactors.PlayerInteractor
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.utils.PlayState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val trackFavInteractor: TrackFavInteractor
) : ViewModel() {

    data class PlayerUiState(
        val playState: PlayState,
        val currentPosition: Long,
        val isFavorite: Boolean = false
    )

    private val _uiState = MutableLiveData<PlayerUiState>().apply {
        value = PlayerUiState(PlayState.Prepared, 0L)
    }
    val uiState: LiveData<PlayerUiState> = _uiState

    private var urlTrack: String = ""
    private var updateJob: Job? = null
    private var currentTrack: Track? = null

    init {
        playerInteractor.setOnCompletionListener {
            _uiState.postValue(
                PlayerUiState(
                    PlayState.Paused,
                    playerInteractor.getCurrentPosition(),
                    _uiState.value?.isFavorite ?: false
                )
            )
        }
    }

    fun setCurrentTrack(track: Track) {
        viewModelScope.launch {
            val isFavorite = trackFavInteractor.isTrackFavorite(track.trackId)
            currentTrack = track.updateFavoriteState(isFavorite)
            _uiState.value = _uiState.value?.copy(isFavorite = isFavorite)
        }
    }

    fun onFavoriteClicked() {
        val track = currentTrack ?: return
        viewModelScope.launch {
            val newFavoriteState = !track.isFavorite
            if (newFavoriteState) {
                trackFavInteractor.addToFavorite(track)
            } else {
                trackFavInteractor.deleteFromFavorites(track)
            }
            currentTrack = track.updateFavoriteState(newFavoriteState)
            _uiState.value = _uiState.value?.copy(isFavorite = newFavoriteState)
            FavoriteManager.notifyFavoriteChanged(track.trackId, newFavoriteState)
        }
    }

    fun getState(): PlayState {
        return _uiState.value?.playState ?: PlayState.Prepared
    }

    fun exit() {
        playerInteractor.exit()
        updateJob?.cancel()
    }

    fun changeState(newState: PlayState) {
        when (newState) {
            PlayState.Playing -> start()
            PlayState.Paused -> pause()
            PlayState.Prepared -> prepare()
        }
        _uiState.value = _uiState.value?.copy(playState = newState)
    }

    private fun prepare() {
        playerInteractor.release()
        playerInteractor.prepare(urlTrack)
        _uiState.value = _uiState.value?.copy(playState = PlayState.Prepared)
    }

    fun setUrlTrack(url: String?) {
        url?.let {
            urlTrack = it
            playerInteractor.release()
        }
    }

    private fun start() {
        try {
            when {
                playerInteractor.getCurrentPosition() == -1L -> {
                    playerInteractor.prepare(urlTrack)
                }

                playerInteractor.getStatePlayer() != PlayState.Playing -> {
                    playerInteractor.start()
                }
            }

            _uiState.value = _uiState.value?.copy(
                playState = PlayState.Playing,
                currentPosition = playerInteractor.getCurrentPosition().coerceAtLeast(0L)
            )

            startUpdatingCurrentPosition()
        } catch (e: IllegalStateException) {
            _uiState.value = _uiState.value?.copy(
                playState = PlayState.Paused,
                currentPosition = 0L
            )

            playerInteractor.release()
            playerInteractor.prepare(urlTrack)
        }
    }

    private fun pause() {
        playerInteractor.pause()
        _uiState.value = _uiState.value?.copy(playState = PlayState.Paused)
        stopUpdatingCurrentPosition()
    }

    private fun startUpdatingCurrentPosition() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            while (true) {
                val currentPosition = playerInteractor.getCurrentPosition()
                _uiState.value = _uiState.value?.copy(currentPosition = currentPosition)
                delay(300)
            }
        }
    }

    private fun stopUpdatingCurrentPosition() {
        updateJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        updateJob?.cancel()
    }
}