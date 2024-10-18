package com.example.playlist__maker

import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesAPI {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") term: String, @Query("entity") entity: String = "song"): SearchResponse
}

data class SearchResponse(
    val resultCount: Int,
    val results: List<Track>
)
