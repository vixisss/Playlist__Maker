package com.example.playlist__maker.player.data

import android.media.MediaPlayer


class PlayerNetwork {

    var trackIsComplete : Boolean = false
    var mediaPlayer : MediaPlayer = MediaPlayer()


    fun prepare(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnCompletionListener {
            trackIsComplete = true
            stop()
        }

    }


    fun getComplete() : Boolean {
        return trackIsComplete
    }


    fun resetComplete() {
        trackIsComplete = false
    }


    fun stop() {
        mediaPlayer.stop()

        mediaPlayer.prepareAsync()
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


    fun getCurrentPosition(): Long{
        return if (mediaPlayer.isPlaying){
            mediaPlayer.currentPosition.toLong()
        } else {
            0L
        }
    }

    fun exit() {
        mediaPlayer.stop()
        mediaPlayer.release()

    }
}