package com.example.playlist__maker.player.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist__maker.player.domain.interactors.PlayerInteractor
import com.example.playlist__maker.utils.PlayState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    data class PlayerUiState(
        val playState: PlayState,
        val currentPosition: Long
    )

    private val _uiState = MutableLiveData<PlayerUiState>()
    val uiState: LiveData<PlayerUiState> get() = _uiState

    private var urlTrack: String = ""
    private var updateJob: Job? = null

    init {
        _uiState.value = PlayerUiState(PlayState.Prepared, 0L)
        playerInteractor.setOnCompletionListener {
            _uiState.postValue(PlayerUiState(PlayState.Paused, playerInteractor.getCurrentPosition()))
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
        playerInteractor.prepare(urlTrack)
        _uiState.value = _uiState.value?.copy(playState = PlayState.Prepared)
    }

    fun setUrlTrack(url: String?) {
        if (url != null) {
            urlTrack = url
        }
    }

    private fun start() {
        if (playerInteractor.getCurrentPosition() == -1L) {
            playerInteractor.prepare(urlTrack)
        } else {
            playerInteractor.start()
        }
        _uiState.value = _uiState.value?.copy(playState = PlayState.Playing)
        startUpdatingCurrentPosition()
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