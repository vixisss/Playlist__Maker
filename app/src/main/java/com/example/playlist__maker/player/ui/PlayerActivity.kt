package com.example.playlist__maker.player.ui

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.ActivityPlayerBinding
import com.example.playlist__maker.utils.PlayState
import com.example.playlist__maker.player.ui.viewModel.PlayerViewModel
import com.example.playlist__maker.search.domain.models.Track
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private val viewModel by viewModel<PlayerViewModel>()

    private val trackObserver = Observer<PlayerViewModel.PlayerUiState> { uiState ->
        binding.time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(uiState.currentPosition)
        updatePlayButtonState(uiState.playState)
        updateFavoriteButtonState(uiState.isFavorite) // Обновляем состояние кнопки "Нравится"
    }

    private var imageState: Map<PlayState, Int> = mapOf(
        PlayState.Playing to R.drawable.player_pause,
        PlayState.Paused to R.drawable.player_play,
        PlayState.Prepared to R.drawable.player_play
    )

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
        viewModel.setCurrentTrack(track) // Устанавливаем текущий трек в ViewModel
    }

    private fun updateCollectionAlbumVisibility() {
        binding.collectionName.visibility = if (track.collectionName.isNotEmpty()) View.VISIBLE else View.GONE
        binding.playerAlbum.visibility = if (track.collectionName.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showPlayer()
        preparePlayer()

        viewModel.uiState.observe(this, trackObserver)

        binding.playerPlayPause.setOnClickListener {
            val currentState = viewModel.getState()
            val newState = when (currentState) {
                PlayState.Playing -> PlayState.Paused
                PlayState.Paused -> PlayState.Playing
                PlayState.Prepared -> PlayState.Playing
            }
            viewModel.changeState(newState)
        }

        // Обработка нажатия на кнопку "Нравится"
        binding.playerLike.setOnClickListener {
            viewModel.onFavoriteClicked()
        }
    }


    private fun preparePlayer() {
        binding.time.text = "00:00"

        track.previewUrl?.let { url ->
            try {
                viewModel.setUrlTrack(url)
                binding.playerPlayPause.setImageResource(imageState[PlayState.Paused]!!)
            } catch (e: IOException) {
                Toast.makeText(this, "Ошибка загрузки трека", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updatePlayButtonState(state: PlayState?) {
        binding.playerPlayPause.setImageResource(imageState[state]!!)
    }

    private fun updateFavoriteButtonState(isFavorite: Boolean) {
        val iconRes = if (isFavorite) R.drawable.player_like_click else R.drawable.player_like
        binding.playerLike.setImageResource(iconRes)
        track.isFavorite = isFavorite // Обновляем состояние трека
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.exit()
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}