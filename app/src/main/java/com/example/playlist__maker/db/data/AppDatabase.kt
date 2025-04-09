package com.example.playlist__maker.db.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist__maker.db.data.playlists.PlaylistDao
import com.example.playlist__maker.db.data.playlists.PlaylistEntity
import com.example.playlist__maker.db.data.tracks.MediaFavDao
import com.example.playlist__maker.db.data.tracks.MediaFavEntity

@Database(
    version = 2,
    entities = [MediaFavEntity::class, PlaylistEntity::class],
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mediaFavDao(): MediaFavDao
    abstract fun playlistDao(): PlaylistDao
}