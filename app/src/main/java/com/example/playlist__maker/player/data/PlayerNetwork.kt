package com.example.playlist__maker.player.data

import android.media.MediaPlayer
import com.example.playlist__maker.utils.PlayState


class PlayerNetwork(
    private var mediaPlayer: MediaPlayer? = null
) {
    private var onCompletionListener: (() -> Unit)? = null

    fun setOnCompletionListener(listener: () -> Unit) {
        onCompletionListener = listener
    }

    fun stop() {
        mediaPlayer?.stop()
    }

    fun prepare(url: String) {
        release()

        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(url)
        mediaPlayer?.prepareAsync()

        mediaPlayer?.setOnPreparedListener {
            start()
        }

        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.seekTo(0) // Сбрасываем позицию на начало
            onCompletionListener?.invoke() // Уведомляем о завершении
        }
    }

    fun start() {
        if (mediaPlayer == null) return

        if (!mediaPlayer!!.isPlaying) {
            mediaPlayer!!.seekTo(mediaPlayer!!.currentPosition)
            mediaPlayer!!.start()
        }
    }

    fun pause() {
        if (mediaPlayer == null) return
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun getCurrentPosition(): Long {
        return mediaPlayer?.currentPosition?.toLong() ?: -1L
    }

    fun exit() {
        mediaPlayer?.release()
    }

    fun getPlayerState(): PlayState {
        return when {
            mediaPlayer == null -> PlayState.Paused
            mediaPlayer!!.isPlaying -> PlayState.Playing
            else -> PlayState.Paused
        }
    }
}