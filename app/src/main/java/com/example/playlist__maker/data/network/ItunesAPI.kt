package com.example.playlist__maker.data.network


import com.example.playlist__maker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesAPI {
    @GET("/search?entity=song")
    fun search(@Query("term") expression: String): Call<TrackSearchResponse>
}

//data class SearchResponse(
//    val resultCount: Int,
//    val results: List<Track>
//): Response()
