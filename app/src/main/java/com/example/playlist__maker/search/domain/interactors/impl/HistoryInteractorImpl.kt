package com.example.playlist__maker.search.domain.interactors.impl

import androidx.lifecycle.LiveData
import com.example.playlist__maker.search.data.dto.History
import com.example.playlist__maker.search.domain.interactors.HistoryInteractor
import com.example.playlist__maker.search.domain.models.Track

class HistoryInteractorImpl(
    private val history: History
) : HistoryInteractor {
    override fun addTrackToHistory(track: Track) {
        history.addTrackToHistory(track)
    }

    override fun updateFavoriteStatus(trackId: String, isFavorite: Boolean) {
        history.updateFavoriteStatus(trackId, isFavorite)
    }

    override fun getHistoryLiveData(): LiveData<List<Track>> {
        return history.historyLiveData
    }

    override fun clearHistory() {
        history.clearHistory()
    }

    override fun showHistoryList(): ArrayList<Track> {
        return history.showHistoryList()
    }
}