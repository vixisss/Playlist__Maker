package com.example.playlist__maker.search.domain

import com.example.playlist__maker.search.domain.models.Track

interface HistoryInteractor {
    fun makeHistoryList(historyList: ArrayList<Track>)
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun showHistoryList(): ArrayList<Track>
}