package com.example.playlist__maker.search.ui.adapter

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist__maker.R
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.utils.Listener
import java.text.SimpleDateFormat
import java.util.Locale

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

    fun bind(model: Track, listenerOnClick: Listener) {
        Glide.with(this.itemView.context)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(dpToPx(2F, itemView.context)))
            .centerCrop()
            .override(dpToPx(45F, itemView.context), dpToPx(45F, itemView.context))
            .into(artworkUrl100View)

        itemView.setOnClickListener {
            listenerOnClick.onClick(model)
        }

        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
    }
}
