package com.example.playlist__maker.media.state

import com.example.playlist__maker.search.domain.models.Track

sealed interface FavState {
    data class Content(val data: List<Track>) : FavState
    data class Error(val message : String): FavState
}