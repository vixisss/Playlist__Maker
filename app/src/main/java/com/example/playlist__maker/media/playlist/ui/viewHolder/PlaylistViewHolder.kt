package com.example.playlist__maker.media.playlist.ui.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist__maker.R
import com.example.playlist__maker.db.domain.models.Playlist
import com.example.playlist__maker.media.playlist.ui.adapter.PlaylistAdapter

class PlaylistViewHolder(view: View)
    : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.titleItem)
    private val description: TextView = view.findViewById(R.id.tracksCountItem)
    private val cover: ImageView = view.findViewById(R.id.albumPhotoItem)

    fun bind(playlist: Playlist) {
        title.text = playlist.name


        val count = playlist.tracksCount

        description.text = itemView.context.resources.getQuantityString(
            R.plurals.howManyTracks,
            count,
            count
        )

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