package com.example.playlist__maker.search.domain

import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.search.ui.ResponseErrorType

sealed interface UiState {
    object Loading : UiState
    data class SearchContent(val data: List<Track>) : UiState
    data class HistoryContent(val data: List<Track>) : UiState
    data class Error(val error: ResponseErrorType) : UiState
}