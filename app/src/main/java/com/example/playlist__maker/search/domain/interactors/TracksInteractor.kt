package com.example.playlist__maker.search.domain.interactors

import com.example.playlist__maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface TracksInteractor {
    fun searchTracks(expression: String) : Flow<Pair<List<Track>?, Int>>
}