package com.example.playlist__maker.player.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.FragmentPlayerBinding
import com.example.playlist__maker.db.tracks.FavoriteManager
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import com.example.playlist__maker.media.playlist.ui.viewModel.PlaylistViewModel
import com.example.playlist__maker.player.ui.adapters.TrackInPlaylistAdapter
import com.example.playlist__maker.player.ui.viewModel.PlayerViewModel
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.utils.DpToPx
import com.example.playlist__maker.utils.PlayState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var track: Track
    private val viewModel by viewModel<PlayerViewModel>()
    private val viewModelPlaylist by viewModel<PlaylistViewModel>()
    private lateinit var playlistsAdapter: TrackInPlaylistAdapter

    private val trackObserver = Observer<PlayerViewModel.PlayerUiState> { uiState ->
        binding.time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(uiState.currentPosition)
        updatePlayButtonState(uiState.playState)
        updateFavoriteButtonState(uiState.isFavorite)
    }

    private val favoriteObserver = Observer<Pair<String, Boolean>> { (trackId, isFavorite) ->
        if (track.trackId == trackId) {
            track.isFavorite = isFavorite
            updateFavoriteButtonState(isFavorite)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val playlistsObserver = Observer<List<Playlist>> { playlists ->
        playlistsAdapter.playlists = playlists
        playlistsAdapter.notifyDataSetChanged()
    }

    private var imageState: Map<PlayState, Int> = mapOf(
        PlayState.Playing to R.drawable.player_pause,
        PlayState.Paused to R.drawable.player_play,
        PlayState.Prepared to R.drawable.player_play
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FavoriteManager.favoriteUpdates.observe(viewLifecycleOwner, favoriteObserver)

        arguments?.getString("track")?.let { trackString ->

            track = Gson().fromJson(trackString, Track::class.java)
            showPlayer()
            preparePlayer()
            setupBottomSheet()
            setupRecyclerView()
            exit()

            viewModelPlaylist.loadPlaylists()

            viewModel.uiState.observe(viewLifecycleOwner, trackObserver)
            viewModelPlaylist.playlists.observe(viewLifecycleOwner, playlistsObserver)

            setupClickListeners()

            binding.playerAddPlaylist.setOnClickListener {
                findNavController().navigate(R.id.action_playerFragment_to_createPlaylistFragment)
            }

            binding.playerAddTrack.setOnClickListener {
                viewModelPlaylist.loadPlaylists()
                binding.overlay.isVisible = true
                binding.playlistsBottomSheet.isVisible = true
                BottomSheetBehavior.from(binding.playlistsBottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun exit() {
        binding.toolbarPlayer.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setupClickListeners() {
        binding.playerPlayPause.setOnClickListener {
            val currentState = viewModel.getState()
            val newState = when (currentState) {
                PlayState.Playing -> PlayState.Paused
                PlayState.Paused -> PlayState.Playing
                PlayState.Prepared -> PlayState.Playing
            }
            viewModel.changeState(newState)
        }

        binding.playerLike.setOnClickListener {
            viewModel.onFavoriteClicked()
        }
    }


    private fun setupRecyclerView() {
        playlistsAdapter = TrackInPlaylistAdapter(emptyList()) { playlist ->
            try {
                val tracksInPlaylist = playlist.tracksIdJson?.let { json ->
                    Gson().fromJson(json, Array<Track>::class.java)?.toList() ?: emptyList()
                } ?: emptyList()

                if (tracksInPlaylist.none { it.trackId == track.trackId }) {
                    viewModelPlaylist.addTrackToPlaylist(playlist.id, track)
                    Toast.makeText(context, "Добавлено в плейлист  ${playlist.name}", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.from(binding.playlistsBottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
                } else {
                    Toast.makeText(context, "Трек уже добавлен в плейлист ${playlist.name}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "рек уже добавлен в плейлист ${playlist.name}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.recycleListPlaylists.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playlistsAdapter
        }
    }


    private fun setupBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.overlay.isVisible = true
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.isVisible = true
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }


    private fun showPlayer() {
        updateTrackInfo()
        updateAlbumArtwork()
        viewModel.setCurrentTrack(track)
    }


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
            .transform(RoundedCorners(DpToPx.dpToPx(8F, requireContext())))
            .into(binding.artworkUrl100)
    }


    private fun updateCollectionAlbumVisibility() {
        binding.collectionName.isVisible = track.collectionName.isNotEmpty()
        binding.playerAlbum.isVisible = track.collectionName.isNotEmpty()
    }


    private fun preparePlayer() {
        binding.time.text = "00:00"

        track.previewUrl?.let { url ->
            try {
                viewModel.setUrlTrack(url)
                imageState[PlayState.Paused]?.let { pausedImageRes ->
                    binding.playerPlayPause.setImageResource(pausedImageRes)
                }
            } catch (e: IOException) {
                Toast.makeText(context, "Ошибка загрузки трека", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun updatePlayButtonState(state: PlayState?) {
        binding.playerPlayPause.setImageResource(imageState[state]!!)
    }


    private fun updateFavoriteButtonState(isFavorite: Boolean) {
        val iconRes = if (isFavorite) R.drawable.player_like_click else R.drawable.player_like
        binding.playerLike.setImageResource(iconRes)
        viewModel.uiState.observe(viewLifecycleOwner) {
            track.isFavorite
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (activity?.isChangingConfigurations != true) {
            viewModel.exit()
            _binding = null
        }
    }
}