package com.example.playlist__maker.data.dto

import com.example.playlist__maker.domain.models.Track


class TrackSearchResponse (
    val resultCount: Int,
    val results: List<Track>): Response()