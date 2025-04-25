package com.example.playlist__maker.search.data.dto

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlist__maker.media.favorite.domain.TrackFavInteractor
import com.example.playlist__maker.search.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import androidx.core.content.edit

class History(
    private val sharedPreferences: SharedPreferences,
    private val trackFavInteractor: TrackFavInteractor
) {
    companion object {
        const val HISTORY_KEY = "history_key"
    }

    private val _historyLiveData = MutableLiveData<List<Track>>()
    val historyLiveData: LiveData<List<Track>> = _historyLiveData

    private val historyList = mutableListOf<Track>()

    init {
        loadHistory()
    }

    fun addTrackToHistory(track: Track) {
        val existingIndex = historyList.indexOfFirst { it.trackId == track.trackId }
        val isFavorite = runBlocking { trackFavInteractor.isTrackFavorite(track.trackId) }
        val trackToAdd = track.copy(isFavorite = isFavorite)

        if (existingIndex != -1) {
            historyList.removeAt(existingIndex)
        }

        historyList.add(0, trackToAdd)

        if (historyList.size > 10) {
            historyList.removeAt(historyList.lastIndex)
        }

        saveHistory()
        updateLiveData()
    }

    fun updateFavoriteStatus(trackId: String, isFavorite: Boolean) {
        historyList.find { it.trackId == trackId }?.isFavorite = isFavorite
        saveHistory()
        updateLiveData()
    }

    private fun updateLiveData() {
        _historyLiveData.postValue(historyList.toList())
    }

    private fun saveHistory() {
        val json = Gson().toJson(historyList)
        sharedPreferences.edit() { putString(HISTORY_KEY, json) }
    }

    private fun loadHistory() {
        val json = sharedPreferences.getString(HISTORY_KEY, null)
        if (!json.isNullOrEmpty()) {
            val loaded = Gson().fromJson(json, Array<Track>::class.java)?.toList() ?: emptyList()
            historyList.clear()
            historyList.addAll(loaded.map { track ->
                val isFavorite = runBlocking { trackFavInteractor.isTrackFavorite(track.trackId) }
                track.copy(isFavorite = isFavorite)
            })
            updateLiveData()
        }
    }

    fun clearHistory(){
        historyList.clear()
        sharedPreferences.edit() { putString(HISTORY_KEY, "") }
    }
    fun showHistoryList(): ArrayList<Track> = ArrayList(historyList)
}