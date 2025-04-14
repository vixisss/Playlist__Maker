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
import com.example.playlist__maker.db.domain.models.Playlist

class TrackInPlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.PlaylistNameItemPlayer)
    private val description: TextView = view.findViewById(R.id.SongsCountItemPlayer)
    private val cover: ImageView = view.findViewById(R.id.artworkUrl100Playlist)


    private val cornerRadius = dpToPx(2f, itemView.context)

    @SuppressLint("UseCompatLoadingForDrawables")
    fun bind(playlist: Playlist) {
        title.text = playlist.name

        val count = playlist.tracksCount

        description.text = itemView.context.resources.getQuantityString(
            R.plurals.howManyTracks,
            count,
            count
        )


        val targetSize = dpToPx(45f, itemView.context)

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