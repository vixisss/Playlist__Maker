package com.example.playlist__maker.player.domain.impl

import com.example.playlist__maker.player.data.PlayerNetwork
import com.example.playlist__maker.player.domain.interactors.PlayerInteractor



class PlayerInteractorImpl(private val context: PlayerNetwork) : PlayerInteractor {

    override fun stop() {
        context.stop()
    }


    override fun resetComplete() {
        context.resetComplete()
    }


    override fun prepare(url: String) {
        context.prepare(url)
    }


    override fun start() {
        context.start()
    }


    override fun pause() {
        context.pause()
    }


    override fun getComplete(): Boolean {
        return context.getComplete()
    }


    override fun getCurrentPosition(): Long = context.getCurrentPosition()


    override fun release() {
        context.release()
    }

    override fun exit() {
        context.exit()
    }
}