package com.example.playlist__maker.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlist__maker.R
import androidx.navigation.fragment.findNavController
import com.example.playlist__maker.databinding.FragmentPlaylistsBinding

class PlaylistsFragment :Fragment() {
    companion object {
        fun newInstance() = PlaylistsFragment()
    }
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

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
        binding.mediaPlaylistLayout.visibility = View.VISIBLE
        binding.mediaAddPlaylist.visibility = View.VISIBLE
        binding.mediaPlaceholderPlaylist.visibility = View.VISIBLE

        binding.mediaAddPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment2)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}