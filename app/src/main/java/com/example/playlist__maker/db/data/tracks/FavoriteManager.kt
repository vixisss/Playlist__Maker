package com.example.playlist__maker.db.data.tracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object FavoriteManager {
    private val _favoriteUpdates = MutableLiveData<Pair<String, Boolean>>()
    val favoriteUpdates: LiveData<Pair<String, Boolean>> = _favoriteUpdates

    fun notifyFavoriteChanged(trackId: String, isFavorite: Boolean) {
        _favoriteUpdates.postValue(trackId to isFavorite)
    }
}