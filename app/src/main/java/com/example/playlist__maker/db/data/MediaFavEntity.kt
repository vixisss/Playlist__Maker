package com.example.playlist__maker.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mediaFav_table")
data class MediaFavEntity (
    @PrimaryKey
    val trackId: String,
    val trackName: String,
    val artistName: String?,
    val trackTime: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String = "",

    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String?,
    val previewUrl: String?
)
