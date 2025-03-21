package com.example.playlist__maker.utils

import com.example.playlist__maker.search.domain.models.Track

sealed interface UiState {
    object Loading : UiState
    data class SearchContent(val data: List<Track>) : UiState
    data class HistoryContent(val data: List<Track>) : UiState
    data class Error(val error: ResponseErrorType) : UiState
}