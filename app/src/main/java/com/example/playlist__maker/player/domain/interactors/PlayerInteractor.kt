package com.example.playlist__maker.player.domain.interactors

import com.example.playlist__maker.player.domain.models.PlayState

interface PlayerInteractor {
    fun prepare(url: String)
    fun start()
    fun pause()
    fun getCurrentPosition(): Long
    fun release()

    fun getStatePlayer() : PlayState

    fun setOnCompletionListener(listener: () -> Unit)

    fun stop()
    fun exit()
}