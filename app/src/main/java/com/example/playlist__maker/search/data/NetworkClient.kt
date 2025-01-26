package com.example.playlist__maker.search.data

import com.example.playlist__maker.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}