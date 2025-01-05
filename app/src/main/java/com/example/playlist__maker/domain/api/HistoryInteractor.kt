package com.example.playlist__maker.domain.api

import com.example.playlist__maker.domain.models.Track

interface HistoryInteractor {
    fun makeHistoryList(historyList: ArrayList<Track>)
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun showHistoryList(): ArrayList<Track>
}