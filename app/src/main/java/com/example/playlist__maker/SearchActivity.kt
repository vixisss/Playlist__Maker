package com.example.playlist__maker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
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


class SearchActivity : AppCompatActivity() {
    private val baseURL = "https://itunes.apple.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesAPIService = retrofit.create(ItunesAPI::class.java)
    private var saveText: String = FIRST_STRING

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val FIRST_STRING = ""
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, saveText)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        saveText = savedInstanceState.getString(SEARCH_TEXT, FIRST_STRING)
    }


    private lateinit var rollback: Toolbar
    private lateinit var clearButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var recycler: RecyclerView
    private lateinit var placeholderTextMessageError: TextView
    private lateinit var placeholderLayout: LinearLayout
    private lateinit var placeholderImg: ImageView
    private lateinit var updateButton: Button
    private lateinit var adapter: TrackAdapter


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


        clearFun()
        simpleTextWatcherFun()
        showRecycler()
        search()
    }



    @SuppressLint("ClickableViewAccessibility")
    private fun clearFun() {
        rollback.setNavigationOnClickListener {
            finish()
        }
        clearButton.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    searchEditText.text.clear()
                    hideKeyboard()
                }
            }
            v?.onTouchEvent(event) ?: true
        }
    }

    private fun simpleTextWatcherFun(){
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)
    }


    private fun showRecycler() {
        adapter = TrackAdapter(emptyList())
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun stopSearch() {
        recycler.visibility = View.GONE
        placeholderLayout.visibility = View.GONE
    }

    private fun hideKeyboard() {
        val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hideKeyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }


    private fun search() {
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                placeholderOrResult()
                hideKeyboard()
                true
            } else {
                false

            }
        }


        clearButton.setOnClickListener{
            searchEditText.text.clear()
            clearButton.visibility = View.GONE
            hideKeyboard()
            stopSearch()
        }


        updateButton.setOnClickListener {
            placeholderOrResult()
        }
    }


    private fun placeholderOrResult() {
        val term = searchEditText.text.toString().trim()
        if (term.isNotEmpty()) {
            lifecycleScope.launch {
                try {
                    val response = searchTracks(term)
                    if (response.results.isEmpty()) {
                        placeholderLayout.visibility = View.VISIBLE
                        placeholderImg.visibility = View.VISIBLE
                        placeholderTextMessageError.visibility = View.VISIBLE

                        recycler.visibility = View.GONE
                        updateButton.visibility = View.GONE

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

                    recycler.visibility = View.GONE

                    placeholderImg.setImageResource(R.drawable.placeholder_nointernet_day)
                    placeholderTextMessageError.text = getString(R.string.no_internet)
                }
            }
        } else {
            stopSearch()
        }
    }


    private fun showResults(tracks: List<Track>) {
        adapter.newTracks(tracks)

        recycler.visibility = View.VISIBLE
        placeholderLayout.visibility = View.VISIBLE

        placeholderImg.visibility = View.GONE
        placeholderTextMessageError.visibility = View.GONE
        updateButton.visibility = View.GONE
    }

    private suspend fun searchTracks(term: String): SearchResponse {
        return itunesAPIService.search(term)
    }

}