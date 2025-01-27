package com.example.playlist__maker.player.ui.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist__maker.creator.Creator
import com.example.playlist__maker.player.domain.interactors.PlayerInteractor
import com.example.playlist__maker.player.domain.models.PlayState


class PlayerViewModel(
        private val playerInteractor: PlayerInteractor) : ViewModel() {

    private var state : MutableLiveData<PlayState> = MutableLiveData<PlayState>(PlayState.Paused)
    private var urlTrack : String = ""

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    PlayerViewModel(
                        playerInteractor = Creator.providePlayer(),
                        )

                }
            }
        }

    }

    fun getState() : PlayState {
        return playerInteractor.getStatePlayer()
    }

    fun exit() {
        playerInteractor.exit()
    }

    fun changeState(state : PlayState) {
        when(state) {
            PlayState.Playing -> { start() }
            PlayState.Paused -> { pause() }
        }
    }


    fun setUrlTrack(url : String?) {
        if (url != null) {
            urlTrack = url
        }
    }

    private fun start() {
        if (playerInteractor.getCurrentPosition() == -1L) {
            playerInteractor.prepare(urlTrack)
        }
        else {
            playerInteractor.start()
        }

        state.value = PlayState.Playing
    }


    private fun pause() {
        playerInteractor.pause()
        state.value = (PlayState.Paused)
    }


    fun getCurrentPosition(): LiveData<Long> {
        return MutableLiveData(playerInteractor.getCurrentPosition())
    }

}





