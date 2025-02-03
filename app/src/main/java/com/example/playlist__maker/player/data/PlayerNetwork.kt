package com.example.playlist__maker.player.data

import android.media.MediaPlayer
import com.example.playlist__maker.player.domain.models.PlayState


class PlayerNetwork (
    private var mediaPlayer: MediaPlayer? = null
){
    fun stop() {
        mediaPlayer!!.stop()
    }

    fun prepare(url: String) {
        release()

        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(url)
        mediaPlayer?.prepareAsync()

        mediaPlayer!!.setOnPreparedListener {
            start()
        }

        mediaPlayer!!.setOnCompletionListener {
            mediaPlayer!!.seekTo(0)
        }

    }

    fun start() {
        if (mediaPlayer == null) { return }

        if (!mediaPlayer!!.isPlaying()) {
            mediaPlayer!!.seekTo(mediaPlayer!!.currentPosition)
            mediaPlayer!!.start()
        }
    }


    fun pause() {
        if (mediaPlayer == null) { return }
        if (mediaPlayer!!.isPlaying()) {
            mediaPlayer!!.pause()
        }
    }


    fun release() {
        if (mediaPlayer == null) {return}

        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun getCurrentPosition() : Long {
        if (mediaPlayer == null) { return -1L }

        return mediaPlayer!!.currentPosition.toLong()
    }

    fun exit() {
        if (mediaPlayer == null) { return }
        mediaPlayer!!.release()
    }

    fun getPlayerState() : PlayState {
        if (mediaPlayer == null) {
            return PlayState.Paused
        }
        if (mediaPlayer!!.isPlaying()) {
            return PlayState.Playing
        }

        return PlayState.Paused
    }
}