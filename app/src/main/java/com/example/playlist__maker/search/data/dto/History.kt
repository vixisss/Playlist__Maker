package com.example.playlist__maker.search.data.dto


import android.content.SharedPreferences
import com.example.playlist__maker.search.domain.models.Track
import com.google.gson.Gson

class History(private val sharedPreferences: SharedPreferences){
    companion object {
        const val HISTORY_KEY = "key"
    }
   private val historyList = ArrayList<Track>()

    fun clearHistory(){
        historyList.clear()
        sharedPreferences.edit().putString(HISTORY_KEY, "").apply()
    }

    fun addTrackToHistory(track: Track){
        val index: Int = historyList.indexOf(track)
        if (index in 0..10){
            historyList.removeAt(index)
        }
        historyList.add(0, track)
        if (historyList.size > 10) {
            historyList.removeAt(10)
        }
        val json = Gson().toJson(historyList)

        sharedPreferences.edit().putString(HISTORY_KEY, json).apply()
    }

    fun makeHistoryList(historyList: ArrayList<Track>){
        if (historyList.isNotEmpty()) {
            val history = Gson().toJson(historyList)
            sharedPreferences.edit()
                .putString(HISTORY_KEY, history)
                .apply()
        }
    }

    fun showHistoryList(): ArrayList<Track> = historyList

    private fun saveAfterExit(){
        val json = sharedPreferences.getString(HISTORY_KEY, null)
        if (!json.isNullOrEmpty()) {
            val history = Gson().fromJson(json, Array<Track>::class.java)
            if (!history.isNullOrEmpty()) {
                historyList.addAll(history)
            }
        }
    }
    init {
        saveAfterExit()
    }
}

