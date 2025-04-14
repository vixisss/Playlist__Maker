package com.example.playlist__maker.search.data.dto

import com.example.playlist__maker.search.data.NetworkClient
import com.example.playlist__maker.search.data.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitBuild(
    private val imdbService: ItunesAPI,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is TrackSearchRequest){
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO){
            try {
                val resp = imdbService.search(dto.expression)
                resp.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }
}


