package com.example.playlist__maker.search.domain.impl

import com.example.playlist__maker.search.data.History
import com.example.playlist__maker.search.domain.HistoryInteractor
import com.example.playlist__maker.search.domain.models.Track

class HistoryInteractorImpl(
    private val history: History
) : HistoryInteractor {
    override fun makeHistoryList(historyList: ArrayList<Track>) {
        history.makeHistoryList(historyList)
    }

    override fun addTrackToHistory(track: Track) {
        history.addTrackToHistory(track)
    }

    override fun clearHistory() {
        history.clearHistory()
    }

    override fun showHistoryList(): ArrayList<Track> {
        return history.showHistoryList()
    }
}