package com.example.playlist__maker.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.FragmentMediaBinding
import com.example.playlist__maker.media.ui.MediaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {
    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!
    private var tabMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
    }

    private fun setupViewPager() {
        // Создаем адаптер для ViewPager
        val adapter = MediaViewPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
            fragments = createFragments()
        )

        binding.viewPager.adapter = adapter

        // Настраиваем TabLayout
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favTracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }.apply {
            attach()
        }
    }

    private fun createFragments(): List<Fragment> {
        return listOf(
            FavTracksFragment.newInstance(),
            PlaylistsFragment.newInstance()
        )
    }

    override fun onDestroyView() {
        tabMediator?.detach()
        tabMediator = null
        _binding = null
        super.onDestroyView()
    }
}