package com.example.playlist__maker.player.domain.interactors

interface PlayerInteractor {
    fun prepare(url: String)
    fun start()
    fun pause()
    fun getCurrentPosition(): Long
    fun release()
    fun getComplete() : Boolean
    fun resetComplete()
    fun stop()
    fun exit()
}