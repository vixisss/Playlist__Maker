package com.example.playlist__maker.domain.models

import java.io.Serializable

data class Track (
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

): Serializable {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}