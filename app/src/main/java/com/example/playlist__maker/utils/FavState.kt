package com.example.playlist__maker.utils

import com.example.playlist__maker.search.domain.models.Track

sealed interface FavoriteState {

    data class Content(val tracks: List<Track>) : FavoriteState

    data object Empty : FavoriteState
}