package com.example.playlist__maker.player.domain.impl

import com.example.playlist__maker.player.data.PlayerNetwork
import com.example.playlist__maker.player.domain.interactors.PlayerInteractor
import com.example.playlist__maker.utils.PlayState


class PlayerInteractorImpl(
    private val context: PlayerNetwork
) : PlayerInteractor {

    override fun stop() {
        context.stop()
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

    override fun getCurrentPosition(): Long {
        return try {
            context.getCurrentPosition()
        } catch (e: IllegalStateException) {
            -1L
        }
    }

    override fun release() {
        context.release()
    }

    override fun exit() {
        context.exit()
    }

    override fun getStatePlayer(): PlayState {
        return try {
            context.getPlayerState()
        } catch (e: IllegalStateException) {
            PlayState.Paused
        }
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        context.setOnCompletionListener(listener)
    }
}