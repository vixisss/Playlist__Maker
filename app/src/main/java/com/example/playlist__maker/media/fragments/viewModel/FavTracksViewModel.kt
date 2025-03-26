package com.example.playlist__maker.media.fragments.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist__maker.db.domain.interactor.TrackFavInteractor
import com.example.playlist__maker.search.domain.models.Track
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavTracksViewModel(
    private val trackFavInteractor: TrackFavInteractor
) : ViewModel() {

    private val _uiState = MutableLiveData<FavoritesUiState>()
    val uiState: LiveData<FavoritesUiState> get() = _uiState

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            trackFavInteractor.getFavTracks().collect { tracks ->
                _uiState.value = if (tracks.isEmpty()) {
                    FavoritesUiState.Empty
                } else {
                    FavoritesUiState.Success(tracks)
                }
            }
        }
    }

    sealed class FavoritesUiState {
        object Empty : FavoritesUiState()
        data class Success(val tracks: List<Track>) : FavoritesUiState()
    }
}