package com.example.playlist__maker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity(), Listener{
    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val FIRST_STRING = ""

        const val HISTORY_PREFERENCES = "HISTORY_PREFERENCES"
    }


    private val baseURL = "https://itunes.apple.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, saveText)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        saveText = savedInstanceState.getString(SEARCH_TEXT, FIRST_STRING)
    }

    private val itunesAPIService = retrofit.create(ItunesAPI::class.java)
    private var saveText: String = FIRST_STRING


    private lateinit var rollback: Toolbar
    private lateinit var clearButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var recycler: RecyclerView
    private lateinit var placeholderTextMessageError: TextView
    private lateinit var placeholderLayout: LinearLayout
    private lateinit var placeholderImg: ImageView
    private lateinit var updateButton: Button
    private lateinit var adapter: TrackAdapter

    private lateinit var history: History
    private lateinit var historyLayout: LinearLayout
    private lateinit var clearHistory: Button
    private lateinit var historyRecycle: RecyclerView
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var toolbarHistory: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        rollback = findViewById(R.id.search_toolbar)
        clearButton = findViewById(R.id.clearIcon)
        searchEditText = findViewById(R.id.editText_Search)
        recycler = findViewById(R.id.recyclerView_tracksList)
        placeholderTextMessageError = findViewById(R.id.error_message_placeholder)
        placeholderLayout = findViewById(R.id.trackList_layout)
        placeholderImg = findViewById(R.id.img_placeholder)
        updateButton = findViewById(R.id.update)

        historyLayout = findViewById(R.id.history_layout)
        clearHistory = findViewById(R.id.clear_history)
        historyRecycle = findViewById(R.id.historyList)
        toolbarHistory = findViewById(R.id.titleHistory)


        clearFun()
        simpleTextWatcherFun()
        showRecycler()
        search()
        clearHistoryFun()

        if (savedInstanceState == null) {
            searchEditText.requestFocus()
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clearFun() {
        rollback.setNavigationOnClickListener { finish() }
        clearButton.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    searchEditText.text.clear()
                    hideKeyboard()
                    stopSearch()
                }
            }
            v?.onTouchEvent(event) ?: true
        }
    }


    @SuppressLint("CommitPrefEdits")
    private fun clearHistoryFun() {
        history = History(getSharedPreferences(HISTORY_PREFERENCES, Context.MODE_PRIVATE))
        historyAdapter = TrackAdapter(history.makeHistoryList(), this)
        historyRecycle.adapter = historyAdapter
        historyRecycle.layoutManager = LinearLayoutManager(this)

        clearHistory.setOnClickListener {
            getSharedPreferences(HISTORY_PREFERENCES, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()

            history.clearHistory()
            historyLayout.visibility = View.GONE
        }
    }

    private fun updateVisibility() {
        val term = searchEditText.text.toString().trim()
        if (term.isEmpty()) {
            placeholderLayout.visibility = View.GONE
            recycler.visibility = View.GONE
            updateButton.visibility = View.GONE
            historyLayout.visibility = View.VISIBLE
        } else {
            placeholderLayout.visibility = View.GONE
            recycler.visibility = View.VISIBLE
            updateButton.visibility = View.VISIBLE
            historyLayout.visibility = View.GONE
            search()
        }
        if(history.historyTracks.size <= 0){
            placeholderLayout.visibility = View.GONE
            recycler.visibility = View.GONE
            updateButton.visibility = View.GONE
            historyLayout.visibility = View.GONE
        }
    }

    private fun simpleTextWatcherFun() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()){
                    clearButton.visibility = View.GONE
                    search()
                    updateVisibility()
                    if (history.historyTracks.size > 0){
                        historyLayout.visibility = View.VISIBLE
                        search()
                        updateVisibility()
                    } else {
                        historyLayout.visibility = View.GONE
                        search()
                        updateVisibility()
                    }
                } else {
                    clearButton.visibility = View.VISIBLE
                }

            }
            override fun afterTextChanged(s: Editable?) {}
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)
    }


    private fun showRecycler() {
        adapter = TrackAdapter(emptyList(), this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
    }


    private fun stopSearch() {
        recycler.visibility = View.GONE
        placeholderLayout.visibility = View.GONE
        searchEditText.requestFocus()
        historyLayout.visibility = View.VISIBLE
    }


    private fun hideKeyboard() {
        val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hideKeyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }


    private fun search() {
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(searchEditText.text.isNotEmpty()){
                    placeholderOrResult()
                } else {
                    placeholderOrResult()
                }

                true
            } else {
                false
            }
        }

        clearButton.setOnClickListener{
            searchEditText.text.clear()
            clearButton.visibility = View.GONE
            historyLayout.visibility = View.VISIBLE
            hideKeyboard()
            stopSearch()
        }

        updateButton.setOnClickListener {
            placeholderOrResult()
        }

        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus && history.historyTracks.size > 0) {
                historyLayout.visibility = View.VISIBLE
                historyRecycle.visibility = View.VISIBLE
                toolbarHistory.visibility = View.VISIBLE
                clearHistory.visibility = View.VISIBLE
            } else {
                searchEditText.requestFocus()
                historyLayout.visibility = View.GONE
                historyRecycle.visibility = View.GONE
                toolbarHistory.visibility = View.GONE
                clearHistory.visibility = View.GONE
            }
        }

    }


    private fun placeholderOrResult() {
        val term = searchEditText.text.toString().trim()
        if (term.isNotEmpty()) {
            historyLayout.visibility = View.GONE
            lifecycleScope.launch {
                try {
                    val response = searchTracks(term)
                    if (response.results.isEmpty()) {
                        placeholderLayout.visibility = View.VISIBLE
                        placeholderImg.visibility = View.VISIBLE
                        placeholderTextMessageError.visibility = View.VISIBLE

                        recycler.visibility = View.GONE
                        updateButton.visibility = View.GONE
                        historyLayout.visibility = View.GONE

                        searchEditText.requestFocus()

                        placeholderImg.setImageResource(R.drawable.placeholder_nothing_find_day)
                        placeholderTextMessageError.text = getString(R.string.nothing_found)
                    } else {
                        showResults(response.results)
                    }
                } catch (e: Exception) {
                    placeholderLayout.visibility = View.VISIBLE
                    placeholderImg.visibility = View.VISIBLE
                    placeholderTextMessageError.visibility = View.VISIBLE
                    updateButton.visibility = View.VISIBLE

                    hideKeyboard()

                    recycler.visibility = View.GONE

                    placeholderImg.setImageResource(R.drawable.placeholder_nointernet_day)
                    placeholderTextMessageError.text = getString(R.string.no_internet)
                }
            }
        } else {
            placeholderLayout.visibility = View.GONE
            stopSearch()
            historyLayout.visibility = View.VISIBLE
        }
    }


    private fun showResults(tracks: List<Track>) {
        adapter.newTracks(tracks)
        hideKeyboard()

        recycler.visibility = View.VISIBLE
        placeholderLayout.visibility = View.VISIBLE

        placeholderImg.visibility = View.GONE
        placeholderTextMessageError.visibility = View.GONE
        updateButton.visibility = View.GONE
    }


    private suspend fun searchTracks(term: String): SearchResponse {
        return itunesAPIService.search(term)
    }


    override fun onClick(track: Track) {
        history = History(getSharedPreferences(HISTORY_PREFERENCES, Context.MODE_PRIVATE))
        history.addTrack(track)
        historyAdapter.newTracks(history.makeHistoryList())
    }
}