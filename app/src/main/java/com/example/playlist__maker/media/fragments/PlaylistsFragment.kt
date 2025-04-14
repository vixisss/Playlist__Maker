package com.example.playlist__maker.media.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.FragmentPlaylistsBinding
import com.example.playlist__maker.media.ui.PlaylistAdapter
import com.example.playlist__maker.media.viewModel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    companion object {
        fun newInstance() = PlaylistsFragment()
        // testest
    }

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var adapter: PlaylistAdapter

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
        adapter = PlaylistAdapter(emptyList())

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
}