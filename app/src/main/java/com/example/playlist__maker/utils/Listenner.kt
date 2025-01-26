package com.example.playlist__maker.utils

import com.example.playlist__maker.search.domain.models.Track

interface Listener {
    fun onClick(track: Track)
}