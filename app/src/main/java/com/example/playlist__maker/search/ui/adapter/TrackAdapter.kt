package com.example.playlist__maker.search.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist__maker.R
import com.example.playlist__maker.search.domain.models.Track

class TrackAdapter(
    private var tracks: List<Track>,
    private val listenerOnClick: OnTrackClickListener
) :  RecyclerView.Adapter<TrackViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view as ViewGroup)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], listenerOnClick)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun newTracks(newTracks: List<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    interface OnTrackClickListener {
        fun onClick(track: Track)
    }

    fun updateList(newList: List<Track>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = tracks.size
            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(oldPos: Int, newPos: Int) =
                tracks[oldPos].trackId == newList[newPos].trackId
            override fun areContentsTheSame(oldPos: Int, newPos: Int) =
                tracks[oldPos] == newList[newPos]
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        tracks = newList.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

}