package com.example.playlist__maker.media.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist__maker.databinding.FragmentFavTracksBinding
import com.example.playlist__maker.media.fragments.viewModel.FavTracksViewModel
import com.example.playlist__maker.media.state.FavState
import com.example.playlist__maker.player.ui.PlayerActivity
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.search.ui.adapter.TrackAdapter
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavTracksFragment : Fragment(), TrackAdapter.OnTrackClickListener {
    private lateinit var adapter: TrackAdapter
    private var trackList: ArrayList<Track> = arrayListOf()

    companion object {
        fun newInstance() = FavTracksFragment()
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
        showRecycler()


        // для поиска
        val rvItems: RecyclerView = binding.favTracksList
        rvItems.apply {
            adapter = adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        // подписываемся на состояние SearchViewModel
        viewModel.getScreenState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavState.Content -> {
                    showContent(state.data)
                    binding.placeholderText.isVisible = false
                    binding.mediaPlaceholderFavTracks.isVisible = false
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


        var recyclerView: RecyclerView = binding.favTracksList
        recyclerView.visibility = View.GONE
    }

    private fun showContent(data: List<Track>) {
        trackList.clear()
        trackList.addAll(data)
        adapter.updateList(trackList) // Используйте ваш метод обновления в адаптере

        binding.mediaPlaceholderFavTracks.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE
        binding.favTracksList.visibility = if (data.isEmpty()) View.GONE else View.VISIBLE
    }



    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }


    private fun showRecycler() {
        adapter = TrackAdapter(trackList, this) // Передаем текущий список треков
        binding.favTracksList.apply {
            adapter = this@FavTracksFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onClick(track: Track) {
        TODO("Not yet implemented")
        val layoutIntent = Intent(requireContext(), PlayerActivity::class.java)
        val gson = Gson()
        val json = gson.toJson(track)
        layoutIntent.putExtra("track", json)
        startActivity(layoutIntent)
        adapter
    }


}