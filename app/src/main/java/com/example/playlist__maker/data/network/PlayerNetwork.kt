package com.example.playlist__maker.data.network

import android.media.MediaPlayer

class PlayerNetwork {
    val mediaPlayer = MediaPlayer()

    fun prepare(url: String, prepareHandle: () -> Unit) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            prepareHandle()
        }

        mediaPlayer.setOnCompletionListener {
            prepareHandle()
        }
    }

    fun start() {
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun release() {
        mediaPlayer.release()
    }
}