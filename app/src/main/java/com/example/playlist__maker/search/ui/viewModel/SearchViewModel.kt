package com.example.playlist__maker.search.ui.viewModel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist__maker.creator.Creator
import com.example.playlist__maker.search.domain.HistoryInteractor
import com.example.playlist__maker.search.domain.TracksInteractor
import com.example.playlist__maker.search.domain.TracksInteractor.TrackConsumer
import com.example.playlist__maker.search.domain.UiState
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.search.ui.ResponseErrorType
import com.example.playlist__maker.utils.ResponseCode

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L

        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SearchViewModel(
                        tracksInteractor = Creator.provideTracksInteractor(),
                        historyInteractor = Creator.provideHistoryInteractor()
                    )
                }
            }
        }
    }

    private var latestSearchText: String? = null

    private val handler = Handler(Looper.getMainLooper())

    private var tracksState = MutableLiveData<UiState>()

    private fun removeCallbacksAndMessages() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun makeRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            tracksState.postValue(UiState.Loading)
            tracksInteractor.searchTracks(newSearchText, object : TrackConsumer {
                override fun consume(foundTrack: ResponseCode<List<Track>>) {
                    when (foundTrack) {
                        is ResponseCode.ServerError -> {
                            tracksState.postValue(UiState.Error(ResponseErrorType.NO_INTERNET))
                        }
                        is ResponseCode.ClientError -> {
                            tracksState.postValue(UiState.Error(ResponseErrorType.NOTHING_FOUND))
                        }
                        is ResponseCode.Success -> {
                            tracksState.postValue(UiState.SearchContent(data = foundTrack.data))
                        }
                    }
                }
            })
        }
    }

    fun repeatRequest() {
        removeCallbacksAndMessages()
        makeRequest(latestSearchText ?: "")
    }
    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText

        removeCallbacksAndMessages()
        val searchRunnable = Runnable { makeRequest(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun getTracksState(): LiveData<UiState> = tracksState

    fun addTrackToHistory(track: Track) {
        historyInteractor.addTrackToHistory(track)
    }

    fun showHistoryList() = historyInteractor.showHistoryList()

    fun clearHistory() {
        historyInteractor.clearHistory()
    }

    override fun onCleared() {
        super.onCleared()
        removeCallbacksAndMessages()
    }
}