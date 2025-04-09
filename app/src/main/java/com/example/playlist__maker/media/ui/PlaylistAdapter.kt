package com.example.playlist__maker.media.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist__maker.R
import com.example.playlist__maker.db.domain.models.Playlist

class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.titleItem)
    private val description: TextView = view.findViewById(R.id.tracksCountItem)
    private val cover: ImageView = view.findViewById(R.id.albumPhotoItem)

    fun bind(playlist: Playlist) {
        title.text = playlist.name
        description.text = "${playlist.tracksCount} треков"

        // Загрузка обложки или заглушки
        if (!playlist.coverPath.isNullOrEmpty()) {
            Glide.with(itemView)
                .load(playlist.coverPath)
                .transform(RoundedCorners(8))
                .placeholder(R.drawable.big_placeholder)
                .into(cover)
        } else {
            cover.setImageResource(R.drawable.big_placeholder)
        }
    }
}

class PlaylistAdapter(
    var playlists: List<Playlist>,
    private val onPlaylistClick: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { onPlaylistClick(playlists[position]) }
    }

    override fun getItemCount(): Int = playlists.size
}