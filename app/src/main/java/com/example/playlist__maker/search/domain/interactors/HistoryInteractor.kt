package com.example.playlist__maker.search.domain.interactors

import androidx.lifecycle.LiveData
import com.example.playlist__maker.search.domain.models.Track

interface HistoryInteractor {
    fun addTrackToHistory(track: Track)
    fun updateFavoriteStatus(trackId: String, isFavorite: Boolean)
    fun getHistoryLiveData(): LiveData<List<Track>>
    fun clearHistory()
    fun showHistoryList(): ArrayList<Track>
}