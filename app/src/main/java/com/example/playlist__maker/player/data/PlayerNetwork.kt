package com.example.playlist__maker.player.data

import android.media.MediaPlayer
import com.example.playlist__maker.utils.PlayState


class PlayerNetwork(
    private var mediaPlayer: MediaPlayer?
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
            mediaPlayer?.seekTo(0)
            onCompletionListener?.invoke()
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
        mediaPlayer?.let { player ->
            try {
                if (player.isPlaying) {
                    player.stop()
                }
                player.reset()
                player.release()
            } catch (e: Exception) {
            } finally {
                mediaPlayer = null
            }
        }
    }

    fun getCurrentPosition(): Long {
        return mediaPlayer?.currentPosition?.toLong() ?: -1L
    }

    fun exit() {
        release()
    }


    fun getPlayerState(): PlayState {
        return when {
            mediaPlayer == null -> PlayState.Paused
            mediaPlayer!!.isPlaying -> PlayState.Playing
            else -> PlayState.Paused
        }
    }
}