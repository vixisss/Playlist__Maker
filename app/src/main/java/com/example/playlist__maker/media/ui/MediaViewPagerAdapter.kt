package com.example.playlist__maker.media.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlist__maker.media.fragments.FavTracksFragment
import com.example.playlist__maker.media.fragments.PlaylistsFragment

class MediaViewPagerAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavTracksFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }
}