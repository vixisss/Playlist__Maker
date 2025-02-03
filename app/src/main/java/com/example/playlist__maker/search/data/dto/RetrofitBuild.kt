package com.example.playlist__maker.search.data.dto

import com.example.playlist__maker.search.data.NetworkClient
import com.example.playlist__maker.search.data.TrackSearchRequest

class RetrofitBuild(
    private val imdbService: ItunesAPI,
) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            return try {
                val resp = imdbService.search(dto.expression).execute()
                val body = resp.body() ?: Response()
                body.apply { resultCode = resp.code() }
            } catch (ex: Exception) {
                return Response().apply { resultCode = 400 }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}


