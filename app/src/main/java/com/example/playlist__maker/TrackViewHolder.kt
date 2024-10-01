package com.example.playlist__maker

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private val trackNameView: TextView = view.findViewById(R.id.trackName)
    private val artistNameView : TextView = view.findViewById(R.id.artistName)
    private val trackTimeView: TextView = view.findViewById(R.id.trackTime)
    private val artworkUrl100View: ImageView = view.findViewById(R.id.artworkUrl100)


    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    fun bind(model: Track) {
        Glide.with(this.itemView.context)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.no_wifi)
            .transform(RoundedCorners(dpToPx(2F, itemView.context)))
            .centerCrop()
            .override(dpToPx(45F, itemView.context), dpToPx(45F, itemView.context))
            .into(artworkUrl100View)


        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = model.trackTime


    }
}