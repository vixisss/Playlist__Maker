package com.example.playlist__maker.data.network


import android.content.SharedPreferences
import com.example.playlist__maker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class History(private val sharedPreferences: SharedPreferences){
    companion object {
        const val HISTORY_KEY = "key"
    }

    var historyTracks = ArrayList<Track>()

    fun makeHistoryList(): ArrayList<Track> {
        val json = if (sharedPreferences.contains(HISTORY_KEY)) {
            sharedPreferences.getString(HISTORY_KEY, null)
        } else {
            return ArrayList()
        }

        if (json != "") {
            historyTracks = Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
        } else {
            historyTracks = ArrayList()
        }
        return historyTracks
    }

    fun clearHistory() {
        historyTracks.clear()
        sharedPreferences.edit().putString(HISTORY_KEY, "").apply()
    }

    fun addTrack(track: Track) {
        val tracks = makeHistoryList()
        val index: Int = tracks.indexOf(track)

        if (index in 0..10){
            tracks.removeAt(index)
        }

        tracks.add(0, track)

        if (tracks.size > 10) {
            tracks.removeAt(10)
        }

        val json = Gson().toJson(tracks)

        sharedPreferences.edit().putString(HISTORY_KEY, json).apply()
        historyTracks = tracks
    }
}