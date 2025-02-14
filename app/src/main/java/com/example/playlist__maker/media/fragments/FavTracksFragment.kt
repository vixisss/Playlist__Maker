package com.example.playlist__maker.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlist__maker.databinding.FragmentFavTracksBinding
import com.example.playlist__maker.media.fragments.viewModel.FavTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavTracksFragment : Fragment() {
    companion object {
        fun newInstance() = FavTracksFragment()
    }
    private lateinit var binding: FragmentFavTracksBinding
    private val viewModel by viewModel<FavTracksViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavTracksBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mediaFavTracksLayout.visibility = View.VISIBLE
        binding.mediaPlaceholderFavTracks.visibility = View.VISIBLE
    }
}