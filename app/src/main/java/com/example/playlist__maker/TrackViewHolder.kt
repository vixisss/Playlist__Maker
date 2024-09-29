package com.example.playlist__maker

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder (parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)){

    private val trackNameView: TextView
    private val artistNameView : TextView
    private val trackTimeView: TextView
    private val artworkUrl100View: ImageView

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }


    init {
        trackNameView = itemView.findViewById(R.id.trackName)
        artistNameView = itemView.findViewById(R.id.artistName)
        trackTimeView = itemView.findViewById(R.id.trackTime)
        artworkUrl100View = itemView.findViewById(R.id.artworkUrl100)
    }

    fun bind(track: Track) {
        trackNameView.text = track.trackName
        artistNameView.text = track.artistName
        trackTimeView.text = track.trackTime

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(dpToPx(2F, itemView.context)))
            .centerCrop()
            .override(dpToPx(45F, itemView.context), dpToPx(45F, itemView.context))
            .into(artworkUrl100View)
    }
}