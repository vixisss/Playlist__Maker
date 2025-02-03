package com.example.playlist__maker.search.data

import com.example.playlist__maker.search.data.dto.Response
import com.example.playlist__maker.search.data.dto.TrackDto

class TrackSearchResponse (
    val results: ArrayList<TrackDto>
) : Response()