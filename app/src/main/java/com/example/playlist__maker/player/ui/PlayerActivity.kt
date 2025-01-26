package com.example.playlist__maker.player.ui


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.ActivityPlayerBinding
import com.example.playlist__maker.player.domain.models.PlayState
import com.example.playlist__maker.player.ui.viewModel.PlayerViewModel
import com.example.playlist__maker.search.domain.models.Track
import com.google.gson.Gson

import java.io.IOException
import java.text.SimpleDateFormat


import java.util.Locale
class PlayerActivity : AppCompatActivity() {

    private var currentRunnable: Runnable? = null
    private var handler: Handler = Handler(Looper.getMainLooper())

    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track

    private var imageState: Map<PlayState, Int> = mapOf(
        PlayState.Playing to R.drawable.player_pause,
        PlayState.Paused to R.drawable.player_play,
        PlayState.Complete to R.drawable.player_play,
        PlayState.Prepared to R.drawable.player_play)


    private fun updateTrackInfo() {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.collectionName
        binding.trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.primaryGenreName.text = track.primaryGenreName
        binding.country.text = track.country
        binding.releaseDate.text = track.releaseDate.substringBefore('-')

        updateCollectionAlbumVisibility()
    }

    private fun updateAlbumArtwork() {
        val artworkUrl = track.getCoverArtwork()
        Glide.with(this)
            .load(artworkUrl)
            .placeholder(R.drawable.big_placeholder)
            .error(R.drawable.big_placeholder)
            .transform(RoundedCorners(dpToPx(8F, this)))
            .into(binding.artworkUrl100)
    }

    private fun showPlayer() {
        val gson = Gson()
        val trackString = intent.getStringExtra("track")
        track = gson.fromJson(trackString, Track::class.java)

        updateTrackInfo()
        updateAlbumArtwork()
    }

    private fun updateCollectionAlbumVisibility() {
        binding.collectionName.visibility = if (track.collectionName.isNotEmpty()) View.VISIBLE else View.GONE
        binding.playerAlbum.visibility = if (track.collectionName.isNotEmpty()) View.VISIBLE else View.GONE
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, PlayerViewModel.factory())[PlayerViewModel::class.java]

        showPlayer()
        preparePlayer()
        exit()

        binding.playerPlayPause.setOnClickListener {
            val stateModel = viewModel.state.value
            Log.d("StateModel", viewModel.state.value.toString())
            var newStateModel : PlayState = PlayState.Prepared

            when (stateModel) {
                PlayState.Playing -> { newStateModel = PlayState.Paused }
                PlayState.Paused -> { newStateModel = PlayState.Playing }
                PlayState.Prepared -> { newStateModel = PlayState.Playing }
                PlayState.Complete -> { newStateModel = PlayState.Prepared }

                else -> {}
            }

            updatePlayButtonState(newStateModel)
            viewModel.changeState(newStateModel)
            updateCurrentTime()
        }

    }


    private fun exit(){
        binding.playerToolbar.setOnClickListener{

            finish()
        }
    }

    private fun preparePlayer() {
        binding.time.text = "00:00"
        track.previewUrl?.let { url ->
            try {
                viewModel.setUrlTrack(url)
                viewModel.prepare()
                binding.playerPlayPause.setImageResource(imageState[PlayState.Prepared]!!)
            } catch (e: IOException) {
                Toast.makeText(this, "Ошибка загрузки трека", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun updatePlayButtonState(state: PlayState?) {
        binding.playerPlayPause.setImageResource(imageState[state]!!)
    }

    private fun updateCurrentTime() {
        if (viewModel.getComplete()) {
            stopPlayer()
            viewModel.resetComplete()
        }

        if (viewModel.state.value == PlayState.Playing) {

            val currentPosition = viewModel.getCurrentPosition()
            binding.time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
            handler.postDelayed({ updateCurrentTime() }, 300)
        }
    }


    private fun stopTimer() {
        Log.d("PlayerActivity::stopTimer()", "")
        handler.removeCallbacksAndMessages(null)
        currentRunnable = null
    }


    private fun stopPlayer() {

        Log.d("PlayerActivity::stopPlayer()", "private fun stopPlayer()")
        binding.time.text = "00:00"
        stopTimer()

        viewModel.changeState(PlayState.Complete)
        updatePlayButtonState(PlayState.Paused)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.exit()
    }


    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }



    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}