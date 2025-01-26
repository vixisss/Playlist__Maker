package com.example.playlist__maker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist__maker.R
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.databinding.ActivitySearchBinding
import com.example.playlist__maker.player.ui.PlayerActivity
import com.example.playlist__maker.search.domain.UiState
import com.example.playlist__maker.search.ui.adapter.TrackAdapter
import com.example.playlist__maker.search.ui.viewModel.SearchViewModel
import com.example.playlist__maker.utils.Listener
import com.google.gson.Gson


class SearchActivity : AppCompatActivity(), Listener {
    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val FIRST_STRING = ""
        const val HISTORY_PREFERENCES = "HISTORY_PREFERENCES"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true
    private var saveText: String = FIRST_STRING


    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var adapter: TrackAdapter


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SearchViewModel.factory())[SearchViewModel::class.java]


        clearFun()
        simpleTextWatcherFun()
        showRecycler()
        search()
        clearHistoryFun()
        setupDeleteKeyListener()


        if (savedInstanceState == null) {
            binding.editTextSearch.requestFocus()
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }

        viewModel.getTracksState().observe(this) { state ->
            showStateResult(state)
        }
    }


    //history
    private fun showHistory() {
        if (viewModel.showHistoryList().isNotEmpty()){
            binding.historyLayout.visibility = View.VISIBLE
            binding.historyList.visibility = View.VISIBLE
            binding.titleHistory.visibility = View.VISIBLE
            binding.clearHistory.visibility = View.VISIBLE
            historyAdapter.newTracks(viewModel.showHistoryList())
        } else {
            binding.historyLayout.visibility = View.GONE
            binding.historyList.visibility = View.GONE
            binding.titleHistory.visibility = View.GONE
            binding.clearHistory.visibility = View.GONE
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun clearHistoryFun() {
        historyAdapter = TrackAdapter(viewModel.showHistoryList(), this)

        binding.historyList.adapter = historyAdapter
        binding.historyList.layoutManager = LinearLayoutManager(this)

        binding.clearHistory.setOnClickListener {
            getSharedPreferences(HISTORY_PREFERENCES, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()

            viewModel.clearHistory()
            binding.historyLayout.visibility = View.GONE
            binding.progressBarSearch.visibility = View.GONE
        }
    }

    private fun updateVisibility() {
        val term = binding.editTextSearch.text.toString().trim()
        if (term.isEmpty()) {
            binding.trackListLayout.visibility = View.GONE
            binding.recyclerViewTracksList.visibility = View.GONE
            binding.update.visibility = View.GONE
            if(viewModel.showHistoryList().isEmpty()){
                binding.historyLayout.visibility = View.GONE
            }
        } else {
            binding.trackListLayout.visibility = View.GONE
            binding.recyclerViewTracksList.visibility = View.VISIBLE
            binding.update.visibility = View.VISIBLE
            binding.historyLayout.visibility = View.GONE
            search()
        }
        if (viewModel.showHistoryList().isEmpty()){
            binding.trackListLayout.visibility = View.GONE
            binding.recyclerViewTracksList.visibility = View.GONE
            binding.update.visibility = View.GONE
            binding.historyLayout.visibility = View.GONE
        }
    }

    override fun onClick(track: Track) {
        if (clickDebounce()) {
            val layoutIntent = Intent(this, PlayerActivity::class.java)
            val gson = Gson()
            val json = gson.toJson(track)
            layoutIntent.putExtra("track", json)
            startActivity(layoutIntent)
        }

        viewModel.addTrackToHistory(track)
        historyAdapter.newTracks(viewModel.showHistoryList())
    }


    private fun setupDeleteKeyListener() {
        binding.editTextSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (binding.editTextSearch.text.isNotEmpty()) {
                    val text = binding.editTextSearch.text
                    val editable = text as? Editable
                    editable?.delete(text.length - 1, text.length)
                    binding.editTextSearch.setSelection(text.length)

                    showHistory()
                }
            }
            true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clearFun() {
        binding.searchToolbar.setNavigationOnClickListener { finish() }
        binding.clearIcon.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.editTextSearch.text.clear()
                    hideKeyboard()
                    stopSearch()
                    binding.progressBarSearch.visibility = View.GONE
                    binding.editTextSearch.requestFocus()
                    showHistory()
                }
            }
            v?.onTouchEvent(event) ?: true
        }
    }


    private fun simpleTextWatcherFun() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(s.toString())
                if (s.isNullOrBlank()) {
                    binding.clearIcon.visibility = View.GONE
                    viewModel.searchDebounce(s.toString())
                    updateVisibility()
                    showHistory()
                    if (viewModel.showHistoryList().isNotEmpty()){
                        binding.historyLayout.visibility = View.VISIBLE
                        viewModel.searchDebounce(s.toString())
                        updateVisibility()
                    } else {
                        binding.historyLayout.visibility = View.GONE
                        viewModel.searchDebounce(s.toString())
                        updateVisibility()

                    }
                } else {
                    binding.clearIcon.visibility = View.VISIBLE
                }

            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding.editTextSearch.addTextChangedListener(simpleTextWatcher)
    }


    private fun showRecycler() {
        adapter = TrackAdapter(emptyList(), this)
        binding.recyclerViewTracksList.adapter = adapter
        binding.recyclerViewTracksList.layoutManager = LinearLayoutManager(this)

    }


    private fun stopSearch() {
        binding.recyclerViewTracksList.visibility = View.GONE
        binding.trackListLayout.visibility = View.GONE
        binding.editTextSearch.requestFocus()
        binding.historyLayout.visibility = View.VISIBLE
    }


    private fun hideKeyboard() {
        val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hideKeyboard.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
    }


    private fun search() {
        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.repeatRequest()
                true
            }
            false

        }

        binding.clearIcon.setOnClickListener {
            binding.editTextSearch.text.clear()
            binding.clearIcon.visibility = View.GONE
            binding.progressBarSearch.visibility = View.GONE
            binding.historyList.visibility = View.VISIBLE
            binding.editTextSearch.requestFocus()
            showHistory()
            hideKeyboard()
            stopSearch()
        }

        binding.update.setOnClickListener {
            viewModel.repeatRequest()
        }

        binding.editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && viewModel.showHistoryList().isNotEmpty()){
                binding.historyLayout.visibility = View.VISIBLE
                binding.historyList.visibility = View.VISIBLE
                binding.titleHistory.visibility = View.VISIBLE
                binding.clearHistory.visibility = View.VISIBLE
            } else {
                binding.editTextSearch.requestFocus()
                binding.historyLayout.visibility = View.GONE
                binding.historyList.visibility = View.GONE
                binding.titleHistory.visibility = View.GONE
                binding.clearHistory.visibility = View.GONE
            }
        }
    }


    private fun placeholderOrResult(errorType: ResponseErrorType) {
        if (binding.editTextSearch.text.isNotEmpty()) {

            binding.trackListLayout.visibility = View.GONE
            binding.update.visibility = View.GONE
            binding.errorMessagePlaceholder.visibility = View.GONE
            binding.recyclerViewTracksList.visibility = View.GONE
            binding.progressBarSearch.visibility = View.VISIBLE
            binding.historyLayout.visibility = View.GONE

            when (errorType) {
                ResponseErrorType.NO_INTERNET -> {
                    binding.trackListLayout.visibility = View.VISIBLE
                    binding.imgPlaceholder.visibility = View.VISIBLE
                    binding.errorMessagePlaceholder.visibility = View.VISIBLE
                    binding.update.visibility = View.VISIBLE

                    hideKeyboard()

                    binding.recyclerViewTracksList.visibility = View.GONE
                    binding.progressBarSearch.visibility = View.GONE

                    binding.imgPlaceholder.setImageResource(R.drawable.placeholder_nointernet_day)
                    binding.errorMessagePlaceholder.text =
                        getString(R.string.no_internet)
                }
                ResponseErrorType.NOTHING_FOUND -> {
                    binding.trackListLayout.visibility = View.VISIBLE
                    binding.imgPlaceholder.visibility = View.VISIBLE
                    binding.errorMessagePlaceholder.visibility = View.VISIBLE

                    binding.progressBarSearch.visibility = View.GONE
                    binding.recyclerViewTracksList.visibility = View.GONE
                    binding.update.visibility = View.GONE
                    binding.historyLayout.visibility = View.GONE

                    binding.editTextSearch.requestFocus()

                    binding.imgPlaceholder.setImageResource(R.drawable.placeholder_nothing_find_day)
                    binding.errorMessagePlaceholder.text =
                        getString(R.string.nothing_found)
                }
            }

        }
    }



    private fun showStateResult(state: UiState) {
        when (state) {
            is UiState.Loading -> {
                binding.progressBarSearch.visibility = View.VISIBLE
            }
            is UiState.SearchContent -> {
                binding.progressBarSearch.visibility = View.GONE
                showResults(state.data)
            }
            is UiState.HistoryContent -> {
                showHistory()

            }
            is UiState.Error -> {
                binding.progressBarSearch.visibility = View.GONE
                placeholderOrResult(state.error)
            }
        }
    }




    @SuppressLint("NotifyDataSetChanged")
    private fun showResults(trackList: List<Track>) {
        adapter.newTracks(trackList)
        hideKeyboard()
        binding.recyclerViewTracksList.visibility = View.VISIBLE
        binding.trackListLayout.visibility = View.VISIBLE

        binding.imgPlaceholder.visibility = View.GONE
        binding.errorMessagePlaceholder.visibility = View.GONE
        binding.update.visibility = View.GONE
        binding.progressBarSearch.visibility = View.GONE
    }



    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, saveText)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        saveText = savedInstanceState.getString(SEARCH_TEXT, FIRST_STRING)
    }
}