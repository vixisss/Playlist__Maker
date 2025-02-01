package com.example.playlist__maker.search.data

import com.example.playlist__maker.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}