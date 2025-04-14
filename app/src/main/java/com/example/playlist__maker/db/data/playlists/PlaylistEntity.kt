package com.example.playlist__maker.db.data.playlists

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverPath: String?,
    val tracksJson: String,
    val tracksCount: Int
)