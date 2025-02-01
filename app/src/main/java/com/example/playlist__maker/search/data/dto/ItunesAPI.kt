package com.example.playlist__maker.search.data.dto


import com.example.playlist__maker.search.data.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesAPI {
    @GET("/search?entity=song")
    fun search(@Query("term") expression: String): Call<TrackSearchResponse>
}

