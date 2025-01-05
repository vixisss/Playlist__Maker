package com.example.playlist__maker.presentation.utils

import com.example.playlist__maker.domain.models.Track

interface Listener {
    fun onClick(track: Track)
}