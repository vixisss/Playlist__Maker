package com.example.playlist__maker.data.dto

data class TrackDto(
    val trackId: Int,
    val resultType: String,
    val trackName: String,
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
