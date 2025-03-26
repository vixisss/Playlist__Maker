package com.example.playlist__maker.search.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist__maker.db.data.FavoriteManager
import com.example.playlist__maker.db.domain.interactor.TrackFavInteractor
import com.example.playlist__maker.search.domain.interactors.HistoryInteractor
import com.example.playlist__maker.search.domain.interactors.TracksInteractor
import com.example.playlist__maker.utils.UiState
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.utils.ResponseErrorType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val historyInteractor: HistoryInteractor,
    private val trackFavInteractor: TrackFavInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

    init {
        observeFavoriteChanges()
    }

    private var latestSearchText: String? = null
    private val tracksState = MutableLiveData<UiState>()
    private val historyState = MutableLiveData<UiState.HistoryContent>()
    private var searchJob: Job? = null


    private val _syncFavoriteEvent = MutableLiveData<Pair<String, Boolean>>()
    val syncFavoriteEvent: LiveData<Pair<String, Boolean>> = _syncFavoriteEvent

    private fun observeFavoriteChanges() {
        FavoriteManager.favoriteUpdates.observeForever { (trackId, isFavorite) ->
            updateTrackFavoriteState(trackId, isFavorite)
        }
    }

    fun updateTrackFavoriteState(trackId: String, isFavorite: Boolean) {
        // Обновляем поиск
        (tracksState.value as? UiState.SearchContent)?.let { state ->
            val updatedTracks = state.data.map { track ->
                if (track.trackId == trackId) track.copy(isFavorite = isFavorite) else track
            }
            tracksState.postValue(UiState.SearchContent(updatedTracks))
        }

        // Обновляем историю
        historyInteractor.updateFavoriteStatus(trackId, isFavorite)
    }


    private fun makeRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            tracksState.postValue(UiState.Loading)
            viewModelScope.launch {
                tracksInteractor.searchTracks(newSearchText).collect { pair ->
                    handleSearchResponse(pair)
                }
            }
        }
    }

    private fun handleSearchResponse(pair: Pair<List<Track>?, Int>) {
        val (foundTracks, httpStatus) = pair
        when (httpStatus) {
            500 -> tracksState.postValue(UiState.Error(ResponseErrorType.NO_INTERNET))
            404 -> tracksState.postValue(UiState.Error(ResponseErrorType.NOTHING_FOUND))
            200 -> foundTracks?.let {
                tracksState.postValue(UiState.SearchContent(data = it))
            } ?: tracksState.postValue(UiState.Error(ResponseErrorType.NOTHING_FOUND))
        }
    }

    fun repeatRequest() {
        latestSearchText?.let { searchText ->
            searchJob?.cancel()
            viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                makeRequest(searchText)
            }
        }
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)

            if (isActive) {
                makeRequest(changedText)
            }
        }
    }

    fun getTracksState(): LiveData<UiState> = tracksState
    fun getHistoryState(): LiveData<UiState.HistoryContent> = historyState

    fun addTrackToHistory(track: Track) {
        historyInteractor.addTrackToHistory(track)
        updateHistoryState()
    }

    fun showHistoryList() {
        updateHistoryState()
    }

    private fun updateHistoryState() {
        val historyList = historyInteractor.showHistoryList()
        historyState.postValue(UiState.HistoryContent(historyList))
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        updateHistoryState()
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}