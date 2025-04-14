package com.example.playlist__maker.search.data.dto

data class TrackDto(
    val trackId: String,
    val trackName: String = "",
    val artistName: String,
    val trackTime: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String = "",

    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String?,
    val previewUrl: String?

)

