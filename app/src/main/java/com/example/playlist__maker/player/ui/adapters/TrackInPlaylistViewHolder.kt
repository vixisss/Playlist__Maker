package com.example.playlist__maker.player.ui.adapters

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
import com.example.playlist__maker.db.domain.models.Playlist

class TrackInPlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.PlaylistNameItemPlayer)
    private val description: TextView = view.findViewById(R.id.SongsCountItemPlayer)
    private val cover: ImageView = view.findViewById(R.id.artworkUrl100Playlist)

    // Calculate corner radius in pixels (8dp)
    private val cornerRadius = dpToPx(2f, itemView.context)

    fun bind(playlist: Playlist) {
        title.text = playlist.name

        val count = playlist.tracksCount

        val word = when {
            count % 100 in 11..14 -> "треков"
            count % 10 == 1 -> "трек"
            count % 10 in 2..4 -> "трека"
            else -> "треков"
        }

        description.text = "${playlist.tracksCount} ${word}"

        // Calculate target size in pixels (45dp)
        val targetSize = dpToPx(45f, itemView.context)

        // Загрузка обложки или заглушки
        if (!playlist.coverPath.isNullOrEmpty()) {
            Glide.with(itemView)
                .load(playlist.coverPath)
                .transform(
                    CenterCrop(),
                    RoundedCorners(cornerRadius)
                )
                .placeholder(R.drawable.placeholder)
                .override(targetSize, targetSize)
                .into(cover)
        } else {
            cover.setImageResource(R.drawable.placeholder)
            // Apply the same transformations to placeholder
            cover.scaleType = ImageView.ScaleType.CENTER_CROP
            cover.background = itemView.context.getDrawable(R.drawable.rounded_corners)
        }
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}