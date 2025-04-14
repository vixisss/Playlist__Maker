package com.example.playlist__maker.player.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist__maker.R
import com.example.playlist__maker.db.domain.models.Playlist

class TrackInPlaylistAdapter (
    var playlists: List<Playlist>,
    private val onPlaylistClick: (Playlist) -> Unit
) : RecyclerView.Adapter<TrackInPlaylistViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackInPlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.player_playlist_item, parent, false)
        return TrackInPlaylistViewHolder(view)
    }

    override fun onBindViewHolder(
        holder:TrackInPlaylistViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { onPlaylistClick(playlists[position]) }
    }

    override fun getItemCount(): Int = playlists.size
}



