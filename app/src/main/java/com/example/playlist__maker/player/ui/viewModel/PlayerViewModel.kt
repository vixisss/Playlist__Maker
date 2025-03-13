package com.example.playlist__maker.player.ui.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist__maker.player.domain.interactors.PlayerInteractor
import com.example.playlist__maker.utils.PlayState

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val state = MutableLiveData(PlayState.Prepared)
    private var urlTrack: String = ""

    fun getState(): PlayState {
        return state.value ?: PlayState.Prepared
    }

    fun exit() {
        playerInteractor.exit()
    }

    fun changeState(newState: PlayState) {
        when (newState) {
            PlayState.Playing -> start()
            PlayState.Paused -> pause()
            PlayState.Prepared -> prepare()
        }
        state.value = newState
    }

    private fun prepare() {
        playerInteractor.prepare(urlTrack)
        state.value = PlayState.Prepared
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
        state.value = PlayState.Playing
    }

    private fun pause() {
        playerInteractor.pause()
        state.value = PlayState.Paused
    }

    fun getCurrentPosition(): LiveData<Long> {
        return MutableLiveData(playerInteractor.getCurrentPosition())
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    init {
        playerInteractor.setOnCompletionListener {
            state.postValue(PlayState.Paused)
        }
    }
}