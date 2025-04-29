package com.example.playlist__maker.media.favorite.ui

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
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.FragmentFavTracksBinding
import com.example.playlist__maker.media.favorite.ui.viewModel.FavTracksViewModel
import com.example.playlist__maker.media.favorite.state.FavState
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.search.ui.adapter.TrackAdapter
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavTracksFragment : Fragment(), TrackAdapter.OnTrackClickListener {
    private lateinit var adapter: TrackAdapter
    private var trackList: ArrayList<Track> = arrayListOf()
    private var isClickAllowed = true
    private var clickDebounceJob: Job? = null

    companion object {
        fun newInstance() = FavTracksFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var _binding: FragmentFavTracksBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModel<FavTracksViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFavTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mediaFavTracksLayout.visibility = View.VISIBLE
        binding.mediaPlaceholderFavTracks.visibility = View.VISIBLE
        binding.favTracksList.isVisible = true
        showRecycler()

        // подписываемся на состояние SearchViewModel
        viewModel.getScreenState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavState.Content -> {
                    showContent(state.data)
                    binding.placeholderText.isVisible = false
                    binding.mediaPlaceholderFavTracks.isVisible = false
                    binding.favTracksList.isVisible = true
                    showRecycler()
                }

                is FavState.Error -> {
                    showError(state.message)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showError(code: String) {
        trackList.clear()
        adapter.notifyDataSetChanged()

        binding.placeholderText.isVisible = true
        binding.mediaPlaceholderFavTracks.isVisible = true
    }

    private fun showContent(data: List<Track>) {
        trackList.clear()
        trackList.addAll(data)
        adapter.updateList(trackList)

        binding.mediaPlaceholderFavTracks.visibility =
            if (data.isEmpty()) View.VISIBLE else View.GONE
        binding.placeholderText.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE
        binding.favTracksList.visibility = if (data.isEmpty()) View.GONE else View.VISIBLE
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }


    private fun showRecycler() {
        adapter = TrackAdapter(trackList, this)
        binding.favTracksList.apply {
            adapter = this@FavTracksFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onClick(track: Track) {
        if (clickDebounce()) {
            val args = Bundle().apply {
                val gson = Gson()
                putString("track", gson.toJson(track))
            }
            findNavController().navigate(R.id.action_mediaFragment_to_playerFragment, args)
        }
    }



    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickDebounceJob = lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
}