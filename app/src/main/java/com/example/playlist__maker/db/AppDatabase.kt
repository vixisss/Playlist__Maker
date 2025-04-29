package com.example.playlist__maker.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist__maker.db.playlists.PlaylistDao
import com.example.playlist__maker.db.playlists.PlaylistEntity
import com.example.playlist__maker.db.tracks.MediaFavDao
import com.example.playlist__maker.db.tracks.MediaFavEntity

@Database(
    version = 3,
    entities = [MediaFavEntity::class, PlaylistEntity::class],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3)
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mediaFavDao(): MediaFavDao
    abstract fun playlistDao(): PlaylistDao
}