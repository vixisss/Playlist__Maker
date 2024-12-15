package com.example.playlist__maker.data.network

import com.example.playlist__maker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}