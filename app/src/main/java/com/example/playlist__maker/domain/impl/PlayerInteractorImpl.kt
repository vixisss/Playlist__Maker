package com.example.playlist__maker.domain.impl

import android.os.Handler
import android.os.Looper
import com.example.playlist__maker.data.network.PlayerNetwork
import com.example.playlist__maker.domain.api.PlayerInteractor
import java.io.IOException

class PlayerInteractorImpl(private val context: PlayerNetwork) : PlayerInteractor {
    private var url: String? = null
    private var handler: Handler = Handler(Looper.getMainLooper())
    override var playerState = STATE_DEFAULT

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    override fun playbackControl(
        start: () -> Unit,
        pause: () -> Unit,
        complete: () -> Unit
    ) {
        when (playerState) {
            STATE_PLAYING -> {
                pause()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                start()
            }
        }
        complete()
    }

    override fun prepare(url: String) {
        this.url = url
        try {
            context.mediaPlayer.reset()
            context.mediaPlayer.setDataSource(url)
            context.mediaPlayer.prepareAsync()
            context.mediaPlayer.setOnPreparedListener {
                playerState = STATE_PREPARED
            }
            context.mediaPlayer.setOnCompletionListener {
                playerState = STATE_PREPARED
            }
        } catch (e: IOException) {
            throw IOException("Ошибка загрузки трека")
        }
    }

    override fun start() {
        if (playerState == STATE_PREPARED || playerState == STATE_PAUSED) {
            context.start()
            playerState = STATE_PLAYING
        }
    }

    override fun pause() {
        if (playerState == STATE_PLAYING) {
            context.pause()
            playerState = STATE_PAUSED
        }
    }

    override fun getCurrentPosition(): Long = context.mediaPlayer.currentPosition.toLong()

    override fun release() {
        context.release()
        handler.removeCallbacksAndMessages(null)
    }


}
