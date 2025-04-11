package com.example.playlist__maker.search.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.FragmentSearchBinding
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.utils.UiState
import com.example.playlist__maker.search.ui.adapter.TrackAdapter
import com.example.playlist__maker.utils.ResponseErrorType
import com.example.playlist__maker.search.ui.viewModel.SearchViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController


class SearchFragment : Fragment(), TrackAdapter.OnTrackClickListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var isClickAllowed = true
    private var saveText: String = FIRST_STRING

    private val viewModel by viewModel<SearchViewModel>()
    private var clickDebounceJob: Job? = null

    private lateinit var historyAdapter: TrackAdapter
    private lateinit var adapter: TrackAdapter

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val FIRST_STRING = ""
        const val HISTORY_PREFERENCES = "HISTORY_PREFERENCES"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecycler()
        clearFun()
        simpleTextWatcherFun()
        search()
        clearHistoryFun()
        setupDeleteKeyListener()

        binding.editTextSearch.requestFocus()

        if (savedInstanceState == null) {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }


        viewModel.getHistoryState().observe(viewLifecycleOwner) { state ->
            showHistory(state.data)
        }

        viewModel.getTracksState().observe(viewLifecycleOwner) { state ->
            showStateResult(state)
        }
    }

    private fun showHistory(historyList: List<Track>) {
        if (historyList.isNotEmpty() && binding.editTextSearch.text.trim().isEmpty()) {
            binding.historyLayout.isVisible = true
            binding.historyList.isVisible = true
            binding.titleHistory.isVisible = true
            binding.clearHistory.isVisible = true
            historyAdapter.newTracks(historyList)
        } else {
            binding.historyLayout.isVisible = false
            binding.historyList.isVisible = false
            binding.titleHistory.isVisible = false
            binding.clearHistory.isVisible = false
        }
    }


    @SuppressLint("CommitPrefEdits")
    private fun clearHistoryFun() {
        historyAdapter = TrackAdapter(emptyList(), this)

        binding.historyList.adapter = historyAdapter
        binding.historyList.layoutManager = LinearLayoutManager(requireContext())

        binding.clearHistory.setOnClickListener {
            requireContext().getSharedPreferences(HISTORY_PREFERENCES, Context.MODE_PRIVATE)
                .edit {
                    clear()
                }

            viewModel.clearHistory()
            binding.historyLayout.isVisible = false
            binding.progressBarSearch.isVisible = false
        }
    }

    private fun updateVisibility() {
        val term = binding.editTextSearch.text.toString().trim()
        if (term.isEmpty()) {
            binding.trackListLayout.isVisible = false
            binding.recyclerViewTracksList.isVisible = false
            binding.update.isVisible = false
            viewModel.showHistoryList()
        } else {
            binding.trackListLayout.isVisible = false
            binding.recyclerViewTracksList.isVisible = true
            binding.update.isVisible = true
            binding.historyLayout.isVisible = false
            search()
        }
    }

    override fun onClick(track: Track) {
        if (clickDebounce()) {
            lifecycleScope.launch {
                viewModel.addTrackToHistory(track)

                val args = Bundle().apply {
                    putString("track", Gson().toJson(track))
                }
                findNavController().navigate(R.id.action_searchFragment_to_playerFragment, args)

                isClickAllowed = true
                clickDebounceJob?.cancel()
            }
        }
    }

    private fun setupDeleteKeyListener() {
        binding.editTextSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (binding.editTextSearch.text.isNotEmpty()) {
                    val text = binding.editTextSearch.text
                    val editable = text as? Editable
                    editable?.delete(text.length - 1, text.length)
                    binding.editTextSearch.setSelection(text.length)

                    viewModel.showHistoryList()
                }
            }
            true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clearFun() {
        binding.clearIcon.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    true
                }
                MotionEvent.ACTION_UP -> {
                    binding.editTextSearch.clearFocus()
                    binding.editTextSearch.text.clear()
                    hideKeyboard()
                    stopSearch()
                    binding.progressBarSearch.isVisible = false
                    viewModel.showHistoryList()
                    true
                }
                else -> false
            }
        }
    }

    private fun simpleTextWatcherFun() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    binding.clearIcon.isVisible = false
                    updateVisibility()
                } else {
                    binding.clearIcon.isVisible = true
                    viewModel.searchDebounce(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding.editTextSearch.addTextChangedListener(simpleTextWatcher)
    }

    private fun showRecycler() {
        if(binding.editTextSearch.text.isEmpty()){
            adapter = TrackAdapter(emptyList(), this)
            binding.recyclerViewTracksList.adapter = adapter
            binding.recyclerViewTracksList.layoutManager = LinearLayoutManager(requireContext())
        }

    }

    private fun stopSearch() {
        if(binding.editTextSearch.text.isEmpty()){
            binding.recyclerViewTracksList.isVisible = false
            binding.trackListLayout.isVisible = false
            binding.editTextSearch.requestFocus()
            binding.historyLayout.isVisible = true
        }

    }

    private fun hideKeyboard() {
        val hideKeyboard = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hideKeyboard.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
    }

    private fun search() {
        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.repeatRequest()
                binding.editTextSearch.clearFocus()
                true
            }
            false
        }

        binding.update.setOnClickListener {
            viewModel.repeatRequest()
        }

        binding.editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.editTextSearch.text.trim().isEmpty()) {
                viewModel.showHistoryList()
            }
        }
    }

    private fun placeholderOrResult(errorType: ResponseErrorType) {
        if (binding.editTextSearch.text.isNotEmpty()) {
            binding.trackListLayout.isVisible = false
            binding.update.isVisible = false
            binding.errorMessagePlaceholder.isVisible = false
            binding.recyclerViewTracksList.isVisible = false
            binding.progressBarSearch.isVisible = true
            binding.historyLayout.isVisible = false

            when (errorType) {
                ResponseErrorType.NO_INTERNET -> {
                    binding.trackListLayout.isVisible = true
                    binding.imgPlaceholder.isVisible = true
                    binding.errorMessagePlaceholder.isVisible = true
                    binding.update.isVisible = true

                    hideKeyboard()

                    binding.recyclerViewTracksList.isVisible = false
                    binding.progressBarSearch.isVisible = false

                    binding.imgPlaceholder.setImageResource(R.drawable.placeholder_nointernet_day)
                    binding.errorMessagePlaceholder.text = getString(R.string.no_internet)
                }
                ResponseErrorType.NOTHING_FOUND -> {
                    binding.trackListLayout.isVisible = true
                    binding.imgPlaceholder.isVisible = true
                    binding.errorMessagePlaceholder.isVisible = true

                    binding.progressBarSearch.isVisible = false
                    binding.recyclerViewTracksList.isVisible = false
                    binding.update.isVisible = false
                    binding.historyLayout.isVisible = false

                    binding.imgPlaceholder.setImageResource(R.drawable.placeholder_nothing_find_day)
                    binding.errorMessagePlaceholder.text = getString(R.string.nothing_found)
                }
            }
        }
    }

    private fun showStateResult(state: UiState) {
        when (state) {
            is UiState.Loading -> {
                binding.progressBarSearch.isVisible = true
                binding.historyLayout.isVisible = false
            }
            is UiState.SearchContent -> {
                binding.progressBarSearch.isVisible = false
                showResults(state.data)
            }
            is UiState.HistoryContent -> {
                showHistory(state.data)
            }
            is UiState.Error -> {
                binding.progressBarSearch.isVisible = false
                placeholderOrResult(state.error)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showResults(trackList: List<Track>) {
        binding.historyLayout.isVisible = false
        adapter.newTracks(trackList)
        hideKeyboard()
        binding.recyclerViewTracksList.isVisible = true
        binding.trackListLayout.isVisible = true

        binding.imgPlaceholder.isVisible = false
        binding.errorMessagePlaceholder.isVisible = false
        binding.update.isVisible = false
        binding.progressBarSearch.isVisible = false
        stopSearch()
    }

    private fun clickDebounce(): Boolean {
        if (!isClickAllowed) return false

        isClickAllowed = false
        clickDebounceJob?.cancel()
        clickDebounceJob = lifecycleScope.launch {
            delay(CLICK_DEBOUNCE_DELAY)
            isClickAllowed = true
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, saveText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            saveText = it.getString(SEARCH_TEXT, FIRST_STRING)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clickDebounceJob?.cancel()
        _binding = null
    }
}