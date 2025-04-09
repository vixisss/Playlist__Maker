package com.example.playlist__maker.db.data.tracks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist__maker.db.data.tracks.MediaFavEntity

@Dao
interface MediaFavDao {

    // Добавление трека в избранное
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun addTrackInFav(media: MediaFavEntity)

    // Удаление трека из избранного
    @Delete
    suspend fun deleteTrackInFav(media: MediaFavEntity)

    // Получение списка всех избранных треков
    @Query("SELECT * FROM mediaFav_table")
    fun getTrackFav(): List<MediaFavEntity>

    // Получение списка идентификаторов всех избранных треков
    @Query("SELECT trackId FROM mediaFav_table")
    suspend fun getFavTrackId(): List<String>

    @Query("SELECT * FROM mediaFav_table WHERE trackId = :trackId LIMIT 1")
    suspend fun getTrackById(trackId: String): MediaFavEntity?
}