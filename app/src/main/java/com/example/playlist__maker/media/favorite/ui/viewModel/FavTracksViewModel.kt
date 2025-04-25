package com.example.playlist__maker.media.favorite.ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist__maker.R
import com.example.playlist__maker.media.favorite.domain.TrackFavInteractor
import com.example.playlist__maker.media.favorite.state.FavState
import com.example.playlist__maker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavTracksViewModel(
    private val likeTrackListInteractor: TrackFavInteractor,
    private val context: Context
) : ViewModel() {

    private val state = MutableLiveData<FavState>()

    init {
        loadData()
    }
    fun getScreenState(): LiveData<FavState> {
        return state
    }
    fun loadData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                likeTrackListInteractor.getFavTracks()
                    .collect() { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }

        when {
            errorMessage != null -> {
                val error = FavState.Error(
                    message = context.getString(
                        R.string.myEmail
                    )
                )
                state.postValue(error)
            }

            else -> {
                val content = FavState.Content(data = tracks)
                state.postValue(content)
            }
        }
    }
}