package com.example.playlist__maker

data class Track (
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String
)
