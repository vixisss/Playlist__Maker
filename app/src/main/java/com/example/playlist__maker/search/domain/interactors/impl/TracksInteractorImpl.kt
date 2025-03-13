package com.example.playlist__maker.search.domain.interactors.impl

import com.example.playlist__maker.search.domain.interactors.TracksInteractor
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.search.domain.repository.TracksRepository
import com.example.playlist__maker.utils.ResponseCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(
    private val repository: TracksRepository
): TracksInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, Int>> {

        return repository.searchTracks(expression).map { result ->
            when (result){
                is ResponseCode.Success -> {
                    Pair(result.data, result.status)
                }
                is ResponseCode.ClientError -> {
                    Pair(null, result.status)
                }
                is ResponseCode.ServerError -> {
                    Pair(null, result.status)
                }
            }
        }
    }
}