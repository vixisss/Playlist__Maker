package com.example.playlist__maker.media.playlist.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.FragmentPlaylistsBinding
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import com.example.playlist__maker.media.playlist.ui.adapter.PlaylistAdapter
import com.example.playlist__maker.media.playlist.ui.viewModel.PlaylistViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(){
    companion object {
        fun newInstance() = PlaylistsFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var adapter: PlaylistAdapter
    private var isClickAllowed = true
    private var clickDebounceJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        viewModel.loadPlaylists()
    }

    private fun setupRecyclerView() {
        adapter = PlaylistAdapter(emptyList()) { playlist ->
            onPlaylistClick(playlist)
        }

        binding.recyclerViewPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewPlaylists.adapter = adapter
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setupObservers() {
        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            if (playlists.isEmpty()) {
                binding.playlistPlaceholderLayout.isVisible = true
                binding.recyclerViewPlaylists.isVisible = false
            } else {
                binding.playlistPlaceholderLayout.isVisible = false
                binding.recyclerViewPlaylists.isVisible = true
                adapter.playlists = playlists
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupClickListeners() {
        binding.mediaAddPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun onPlaylistClick(playlist: Playlist) {
        if (clickDebounce()) {
            val bundle = Bundle().apply {
                putSerializable("playlist", playlist)
            }
            findNavController().navigate(
                R.id.action_mediaFragment_to_playlistDetailsFragment,
                bundle
            )
        }
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