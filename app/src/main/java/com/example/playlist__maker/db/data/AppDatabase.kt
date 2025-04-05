package com.example.playlist__maker.db.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [MediaFavEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun mediaFavDao(): MediaFavDao
}