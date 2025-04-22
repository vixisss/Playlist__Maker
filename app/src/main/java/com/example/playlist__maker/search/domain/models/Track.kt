package com.example.playlist__maker.search.domain.models

import java.io.Serializable

data class Track (
    val trackId: String = "",
    val trackName: String,
    val artistName: String,
    val trackTime: String?,
    val trackTimeMillis: Int,
    val artworkUrl100: String = "",

    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String?,
    val previewUrl: String?,

    var isFavorite: Boolean = false

): Serializable {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    fun updateFavoriteState(isFavorite: Boolean): Track {
        return this.copy(isFavorite = isFavorite)
    }
}
