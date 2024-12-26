package com.example.playlist__maker.domain.api

interface PlayerInteractor {
    fun prepare(url: String)
    fun start()
    fun pause()
    fun getCurrentPosition(): Long
    fun release()
    fun playbackControl(start: () -> Unit, pause: () -> Unit, complete: () -> Unit)
    var playerState: Int

}
