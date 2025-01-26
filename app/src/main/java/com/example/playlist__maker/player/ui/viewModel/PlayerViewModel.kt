package com.example.playlist__maker.player.ui.viewModel


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
    var state : MutableLiveData<PlayState> = MutableLiveData<PlayState>(PlayState.Prepared)
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

    fun exit() {
        playerInteractor.exit()
    }

    fun changeState(state : PlayState) {
        when(state) {
            PlayState.Playing -> { start() }
            PlayState.Prepared -> { prepare() }
            PlayState.Paused -> { pause() }
            PlayState.Complete -> { stop() }
        }
    }


    fun setUrlTrack(url : String?) {
        if (url != null) {
            urlTrack = url
        }
    }


    fun getComplete() : Boolean {
        return playerInteractor.getComplete()
    }


    fun resetComplete() {
        playerInteractor.resetComplete()
    }


    fun prepare(){
        playerInteractor.prepare(urlTrack)
        state.value = (PlayState.Prepared)
    }


    fun start() {
        playerInteractor.start()
        state.value = PlayState.Playing
    }


    fun pause() {
        playerInteractor.pause()
        state.value = (PlayState.Paused)
    }


    fun getCurrentPosition(): Long {
        return playerInteractor.getCurrentPosition()
    }


    fun stop(){
        playerInteractor.stop()
        state.value = (PlayState.Prepared)
    }

    override fun onCleared() {
        super.onCleared()
    }
}





