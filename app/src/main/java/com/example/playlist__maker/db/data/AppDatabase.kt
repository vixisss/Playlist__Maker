package com.example.playlist__maker.db.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.playlist__maker.db.data.MediaFavEntity



@Database(version = 1, entities = [MediaFavEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun mediaFavDao(): MediaFavDao
}