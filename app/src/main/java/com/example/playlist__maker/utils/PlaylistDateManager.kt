package com.example.playlist__maker.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PlaylistDateManager {
    private const val PREFS_NAME = "playlist_dates"
    private const val KEY_PREFIX = "playlist_date_"

    fun saveCreationDate(context: Context, playlistId: Long, date: Long = System.currentTimeMillis()) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putLong("$KEY_PREFIX$playlistId", date)
            .apply()
    }

    fun getCreationDate(context: Context, playlistId: Long): Long {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getLong("$KEY_PREFIX$playlistId", 0)
    }

    fun getCreationYear(context: Context, playlistId: Long): String {
        val date = getCreationDate(context, playlistId)
        return if (date != 0L) {
            SimpleDateFormat("yyyy", Locale.getDefault()).format(Date(date))
        } else {
            "—"
        }
    }
}