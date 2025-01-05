package com.example.playlist__maker.data.network

import com.example.playlist__maker.data.dto.Response
import com.example.playlist__maker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuild : NetworkClient {
    private val baseURL = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imdbService = retrofit.create(ItunesAPI::class.java)

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


