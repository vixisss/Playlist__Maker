package com.example.playlist__maker.media.playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.PlaylistDetailsFragmentBinding
import com.example.playlist__maker.db.domain.models.Playlist
import com.example.playlist__maker.media.playlist.viewModel.PlaylistViewModel
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.search.ui.adapter.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class PlaylistDetailsFragment : Fragment() {
    private var _binding: PlaylistDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentPlaylist: Playlist

    private var isClickAllowed = true
    private var clickDebounceJob: Job? = null

    private lateinit var trackAdapter: TrackAdapter
    private val viewModel: PlaylistViewModel by viewModel()



    companion object {
        private const val PLAYLIST_ARG_KEY = "playlist"
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun newInstance(playlist: Playlist): PlaylistDetailsFragment {
            return PlaylistDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PLAYLIST_ARG_KEY, playlist)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet)
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        arguments?.let { bundle ->
            currentPlaylist = (bundle.getSerializable(PLAYLIST_ARG_KEY) as? Playlist)!!
            if (currentPlaylist != null) {
                bindPlaylistData()
            } else {
                requireActivity()
            }
        } ?: run {
            requireActivity()
        }

        setupClickListeners()

        trackAdapter = TrackAdapter(emptyList(), object : TrackAdapter.OnTrackClickListener {
            override fun onClick(track: Track) {
                if (clickDebounce()) {
                    lifecycleScope.launch {

                        val args = Bundle().apply {
                            putString("track", Gson().toJson(track))
                        }
                        findNavController().navigate(R.id.action_playlistDetailsFragment_to_playerFragment, args)

                        isClickAllowed = true
                        clickDebounceJob?.cancel()
                    }
                }
            }

            override fun onLongClick(track: Track) {
                //showDeleteTrackDialog(track)
            }
        })

        binding.recycleListPlaylistDetails.adapter = trackAdapter
        binding.recycleListPlaylistDetails.layoutManager = LinearLayoutManager(requireContext())

        viewModel.playlistTracks.observe(viewLifecycleOwner) { tracks ->
            trackAdapter.updateList(tracks)
            binding.playlistsBottomSheet.isVisible = tracks.isNotEmpty()
        }

        arguments?.getSerializable("playlist")?.let { playlist ->
            currentPlaylist = playlist as Playlist
            viewModel.loadPlaylistTracks(currentPlaylist.id)
        }
    }

    private fun bindPlaylistData() {
        with(binding) {
            playlistNameDetails.text = currentPlaylist.name

            totalTracks.text = resources.getQuantityString(
                R.plurals.howManyTracks,
                currentPlaylist.tracksCount,
                currentPlaylist.tracksCount
            )

            viewModel.playlistTracks.observe(viewLifecycleOwner) { tracks ->
                val totalDuration = tracks.sumOf { it.trackTimeMillis ?: 0 }
                totalTime.text = formatTotalDuration(totalDuration)
            }

            currentPlaylist.coverPath?.let { coverPath ->
                Glide.with(requireContext())
                    .load(coverPath)
                    .into(coverDetails)
            } ?: run {
                coverDetails.setImageResource(R.drawable.big_placeholder)
            }
        }
    }

    private fun setupClickListeners() {
        binding.backToolbar.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun formatTotalDuration(millis: Int): String {
        val totalMinutes = TimeUnit.MILLISECONDS.toMinutes(millis.toLong())
        return resources.getQuantityString(R.plurals.howManyMinutes, totalMinutes.toInt(), totalMinutes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickDebounce(): Boolean {
        if (!isClickAllowed) return false

        isClickAllowed = false
        clickDebounceJob?.cancel()
        clickDebounceJob = lifecycleScope.launch {
            delay(CLICK_DEBOUNCE_DELAY)
            isClickAllowed = true
        }
        return true
    }
}