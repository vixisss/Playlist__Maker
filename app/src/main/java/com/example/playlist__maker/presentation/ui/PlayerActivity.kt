package com.example.playlist__maker.presentation.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist__maker.R
import com.example.playlist__maker.domain.api.PlayerInteractor
import com.example.playlist__maker.domain.models.Track
import com.example.playlist__maker.presentation.utils.Creator
import com.google.gson.Gson
import java.io.IOException
import java.text.SimpleDateFormat

import java.util.Locale
class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT
    private var currentRunnable: Runnable? = null
    private var handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var backButton: Toolbar
    private lateinit var artworkUrl312: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTime: TextView
    private lateinit var collectionName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var genreTrack: TextView
    private lateinit var country: TextView
    private lateinit var albumTrack: TextView
    private lateinit var play: ImageButton
    private lateinit var time: TextView
    private lateinit var interactor: PlayerInteractor
    private lateinit var track: Track

    private fun initialComponents(){
        backButton = findViewById(R.id.player_toolbar)
        artworkUrl312 = findViewById(R.id.artworkUrl100)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        trackTime = findViewById(R.id.trackTime)
        collectionName = findViewById(R.id.collectionName)
        releaseDate = findViewById(R.id.releaseDate)
        genreTrack = findViewById(R.id.primaryGenreName)
        country = findViewById(R.id.country)
        albumTrack = findViewById(R.id.player_album)
        play = findViewById(R.id.player_play_pause)
        time = findViewById(R.id.time)
    }

    private fun updateTrackInfo() {
        findViewById<TextView>(R.id.trackName).text = track.trackName
        findViewById<TextView>(R.id.artistName).text = track.collectionName
        findViewById<TextView>(R.id.trackTime).text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        findViewById<TextView>(R.id.primaryGenreName).text = track.primaryGenreName
        findViewById<TextView>(R.id.country).text = track.country
        findViewById<TextView>(R.id.releaseDate).text = track.releaseDate.substringBefore('-')

        updateCollectionAlbumVisibility()
    }

    private fun updateAlbumArtwork() {
        val artworkUrl = track.getCoverArtwork()
        Glide.with(this)
            .load(artworkUrl)
            .placeholder(R.drawable.big_placeholder)
            .error(R.drawable.big_placeholder)
            .transform(RoundedCorners(dpToPx(8F, this)))
            .into(findViewById(R.id.artworkUrl100))
    }

    private fun updateCollectionAlbumVisibility() {
        findViewById<TextView>(R.id.collectionName).visibility = if (track.collectionName.isNotEmpty()) View.VISIBLE else View.GONE
        findViewById<View>(R.id.player_album).visibility = if (track.collectionName.isNotEmpty()) View.VISIBLE else View.GONE
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        interactor = Creator.providePlayer()
        time = findViewById(R.id.time)
        play = findViewById(R.id.player_play_pause)

        initialComponents()
        showPlayer()
        preparePlayer()
        setupPlayButtonListener()
        exit()

        play.setOnClickListener {
            updatePlayButtonState()
        }

    }


    private fun exit(){
        backButton.setOnClickListener{
            finish()
        }
    }

    private fun showPlayer() {
        val gson = Gson()
        val trackString = intent.getStringExtra("track")
        track = gson.fromJson(trackString, Track::class.java)

        updateTrackInfo()
        updateAlbumArtwork()
    }


    private fun preparePlayer() {
        track.previewUrl?.let { url ->
            try {
                interactor.prepare(url)
            } catch (e: IOException) {
                Toast.makeText(this, "Ошибка загрузки трека", Toast.LENGTH_LONG).show()
            }
        }
    }




    private fun setupPlayButtonListener() {
        play.setOnClickListener {
            interactor.playbackControl(
                start = {
                    startPlayer()
                    if (interactor.playerState == STATE_PREPARED) {
                        startPlayer()
                    }
                },
                pause = { pausePlayer()},
                complete = { updatePlayButtonState() }
            )
        }
    }


    private fun updatePlayButtonState() {
        when (interactor.playerState) {
            STATE_PREPARED -> {
                startPlayer()
                time.text = "00:00"
            }
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PAUSED -> {
                startPlayer()
            }
            else -> {
                stopPlayer()
                time.text = "00:00"
            }
        }
    }



    private fun updateCurrentTime() {
        if (interactor.playerState != STATE_DEFAULT && interactor.playerState != STATE_PREPARED) {
            val currentPosition = interactor.getCurrentPosition()
            time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
            handler.postDelayed({ updateCurrentTime() }, 300)
        } else {
            stopTimer()
            time.text = "00:00"
            play.setImageResource(R.drawable.player_play)
            preparePlayer()
        }
    }

    private fun startTimer() {
        stopTimer()
        currentRunnable = Runnable { updateCurrentTime() }
        handler.postDelayed(currentRunnable!!, 300)
    }

    private fun stopTimer() {
        handler.removeCallbacksAndMessages(null)
        currentRunnable = null
    }




    private fun startPlayer() {
        if (playerState == STATE_PREPARED){
            time.text = "00:00"
        }

        interactor.start()
        play.setImageResource(R.drawable.player_pause)
        playerState = STATE_PLAYING
        startTimer()
    }

    private fun pausePlayer() {
        interactor.pause()
        play.setImageResource(R.drawable.player_play)
        playerState = STATE_PAUSED
    }
    private fun stopPlayer() {
        if (playerState == STATE_PLAYING) {
            interactor.pause()
        }
        playerState = STATE_PAUSED
        stopTimer()
    }




    override fun onPause() {
        super.onPause()
        interactor.pause()
        stopTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        interactor.release()
    }


    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}