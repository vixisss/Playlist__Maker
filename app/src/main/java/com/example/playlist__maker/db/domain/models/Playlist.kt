package com.example.playlist__maker.db.domain.models

import java.io.Serializable

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverPath: String?,
    val tracksIdJson: String,
    val tracksCount: Int,
    val creationDate: Long = System.currentTimeMillis()

) : Serializable
