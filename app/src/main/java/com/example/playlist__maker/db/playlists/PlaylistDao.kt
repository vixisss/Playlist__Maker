package com.example.playlist__maker.db.playlists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert
    suspend fun insert(playlist: PlaylistEntity): Long

    @Query("UPDATE playlists SET tracksJson = :tracksJson, tracksCount = :count WHERE id = :playlistId")
    suspend fun updatePlaylistTracks(playlistId: Long, tracksJson: String, count: Int)

    @Query("SELECT tracksJson FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistTracksJson(playlistId: Long): String?

    @Update
    suspend fun update(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    fun getAllPlaylistsFlow(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistByIdFlow(playlistId: Long): Flow<PlaylistEntity?>

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun delete(playlistId: Long)
}