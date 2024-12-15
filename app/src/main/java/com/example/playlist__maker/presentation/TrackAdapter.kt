package com.example.playlist__maker.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist__maker.R
import com.example.playlist__maker.domain.models.Track

class TrackAdapter(
    private var tracks: List<Track>,
    private val listenerOnClick: Listener
) :  RecyclerView.Adapter<TrackViewHolder>(){

    var trackList = mutableListOf<Track>()

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

}