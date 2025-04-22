package com.example.playlist__maker.media.playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.PlaylistDetailsFragmentBinding
import com.example.playlist__maker.db.domain.models.Playlist


class PlaylistDetailsFragment : Fragment() {
    private var _binding: PlaylistDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentPlaylist: Playlist

    companion object {
        private const val PLAYLIST_ARG_KEY = "playlist"

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
    }

    private fun bindPlaylistData() {
        with(binding) {
            // Заполняем данные плейлиста
            playlistNameDetails.text = currentPlaylist.name
            totalTracks.text = resources.getQuantityString(
                R.plurals.howManyTracks,
                currentPlaylist.tracksCount,
                currentPlaylist.tracksCount
            )

            // Форматируем время (предполагается, что есть метод formatTime)
            //totalTime.text = formatTime(currentPlaylist)

            // Загружаем обложку
            currentPlaylist.coverPath?.let { coverPath ->
                Glide.with(requireContext())
                    .load(coverPath)
                    //.placeholder(R.drawable.big_placeholder)
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

        // Здесь можно добавить другие обработчики кликов
    }

    @SuppressLint("DefaultLocale")
    private fun formatTime(millis: Long): String {
        // Реализуйте форматирование времени по вашему усмотрению
        val seconds = millis / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}