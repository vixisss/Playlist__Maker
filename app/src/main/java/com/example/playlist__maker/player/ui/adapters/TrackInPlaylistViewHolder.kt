package com.example.playlist__maker.player.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist__maker.R
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import com.example.playlist__maker.utils.DpToPx

class TrackInPlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.PlaylistNameItemPlayer)
    private val description: TextView = view.findViewById(R.id.SongsCountItemPlayer)
    private val cover: ImageView = view.findViewById(R.id.artworkUrl100Playlist)

    @SuppressLint("UseCompatLoadingForDrawables")
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
                .transform(
                    CenterCrop(),
                    RoundedCorners(DpToPx.dpToPx(2F, itemView.context))
                )
                .placeholder(R.drawable.placeholder)
                .override(DpToPx.dpToPx(45F, itemView.context), DpToPx.dpToPx(45F, itemView.context))
                .into(cover)
        } else {
            cover.setImageResource(R.drawable.placeholder)
            cover.scaleType = ImageView.ScaleType.CENTER_CROP
            cover.background = itemView.context.getDrawable(R.drawable.rounded_corners)
        }
    }
}