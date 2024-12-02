package com.example.playlist__maker


import android.content.Context
import android.media.MediaPlayer
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
    private var mediaPlayer = MediaPlayer()
    private var url: String? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        initialComponents()

        exit()
        showPlayer()
        preparePlayer()

        play.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }


    private fun updateCurrentTime() {
        if (playerState == STATE_PLAYING) {
            val currentPosition = mediaPlayer.currentPosition

            time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
        }
        handler.postDelayed({ updateCurrentTime() }, 300)
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


    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer() {
        url?.let { url ->
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    play.isEnabled = true
                    playerState = STATE_PREPARED
                }
                mediaPlayer.setOnCompletionListener {
                    play.setImageResource(R.drawable.player_play)
                    playerState = STATE_PREPARED
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Ошибка загрузки трека", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        play.setImageResource(R.drawable.player_pause)
        playerState = STATE_PLAYING
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.setImageResource(R.drawable.player_play)
        playerState = STATE_PAUSED
    }



    private fun exit(){
        backButton.setOnClickListener {
            finish()
        }
    }


    private fun showPlayer(){
        val intent = intent
        val gson = Gson()
        val trackString = intent.getStringExtra("track")
        val track = gson.fromJson(trackString, Track::class.java)
        val bigUrl = track.getCoverArtwork()

        trackName.text = track.trackName
        artistName.text = track.collectionName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())
        genreTrack.text = track.primaryGenreName
        country.text = track.country
        releaseDate.text = track.releaseDate.substringBefore('-')

        if (track.collectionName.isNotEmpty()) {
            collectionName.visibility = View.VISIBLE
            albumTrack.visibility = View.VISIBLE
            collectionName.text = track.collectionName
        } else {
            collectionName.visibility = View.GONE
            albumTrack.visibility = View.GONE
        }

        track.previewUrl?.let {
            url = it
            preparePlayer()
        }


        Glide.with(this)
            .load(bigUrl)
            .placeholder(R.drawable.big_placeholder)
            .error(R.drawable.big_placeholder)
            .transform(RoundedCorners(dpToPx(8F, this)))
            .into(artworkUrl312)
    }


    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}