package com.example.playlist__maker.media.playlist.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Trace.isEnabled
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import com.example.playlist__maker.media.playlist.ui.viewModel.PlaylistViewModel
import com.example.playlist__maker.search.domain.models.Track
import com.example.playlist__maker.search.ui.adapter.TrackAdapter
import com.example.playlist__maker.utils.DpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class PlaylistDetailsFragment : Fragment() {
    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentPlaylist: Playlist

    private var isClickAllowed = true
    private var clickDebounceJob: Job? = null

    private lateinit var trackAdapter: TrackAdapter
    private val viewModel: PlaylistViewModel by viewModel()

    companion object {
        private const val PLAYLIST_ARG_KEY = "playlist"
        private const val KEY_UPDATED_PLAYLIST = "updated_playlist"
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun newInstance(playlist: Playlist): PlaylistDetailsFragment {
            return PlaylistDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PLAYLIST_ARG_KEY, playlist)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        loadPlaylist()
        setupRecycleAdapter()
        handleExit()
        setupObservers()
    }


    private fun setupObservers() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Playlist>(
            KEY_UPDATED_PLAYLIST
        )?.observe(viewLifecycleOwner) { updatedPlaylist ->
            currentPlaylist = updatedPlaylist
            bindPlaylistData()
        }

        arguments?.getSerializable(PLAYLIST_ARG_KEY)?.let {
            currentPlaylist = it as Playlist
            viewModel.loadPlaylistTracks(currentPlaylist.id)
            bindPlaylistData()
        }

        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            playlists.find { it.id == currentPlaylist.id }?.let { updatedPlaylist ->
                currentPlaylist = updatedPlaylist
                bindPlaylistData()
            }
        }

        viewModel.playlistTracks.observe(viewLifecycleOwner) { tracks ->
            trackAdapter.updateList(tracks)
            binding.tracksBottomSheet.isVisible = tracks.isNotEmpty()
        }
    }

    private var backPressedCallback: OnBackPressedCallback? = null

    private fun handleExit() {
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.moreBottomSheet.isVisible) {
                    binding.moreBottomSheet.isVisible = false
                    binding.overlay.isVisible = false
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                    isEnabled = true
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback!!
        )
    }


    private fun setupBottomSheet(){
        val bottomSheetBehaviorMore = BottomSheetBehavior.from(binding.moreBottomSheet)
        bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_COLLAPSED

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet)
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehaviorMore.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.overlay.isVisible = true
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.isVisible = true
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> {}
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }


    private fun loadPlaylist(){
        arguments?.getSerializable("playlist")?.let { playlist ->
            currentPlaylist = playlist as Playlist
            viewModel.loadPlaylistTracks(currentPlaylist.id)
        }
    }


    private fun setupRecycleAdapter(){
        trackAdapter = TrackAdapter(emptyList(), object : TrackAdapter.OnTrackClickListener {
            override fun onClick(track: Track) {
                if (clickDebounce()) {
                    lifecycleScope.launch {

                        val args = Bundle().apply {
                            putString("track", Gson().toJson(track))
                        }
                        findNavController().navigate(R.id.action_playlistDetailsFragment_to_playerFragment, args)

                        isClickAllowed = true
                        clickDebounceJob?.cancel()
                    }
                }
            }

            override fun onLongClick(track: Track) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Удалить трек")
                    .setMessage("Хотите удалить трек?")
                    .setNegativeButton("нет") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("да") { dialog, _ ->
                        viewModel.removeTrackFromPlaylist(currentPlaylist.id, track.trackId)
                        bindPlaylistData()
                        dialog.dismiss()
                    }
                    .show()
            }
        })


        binding.recycleListPlaylistDetails.adapter = trackAdapter
        binding.recycleListPlaylistDetails.layoutManager = LinearLayoutManager(requireContext())

        viewModel.playlistTracks.observe(viewLifecycleOwner) { tracks ->
            trackAdapter.updateList(tracks)
            binding.tracksBottomSheet.isVisible = tracks.isNotEmpty()
        }
    }


    private fun bindPlaylistData() {
        with(binding) {
            shareDetails.post {
                val location = IntArray(2)
                shareDetails.getLocationOnScreen(location)
                val shareDetailsBottom = location[1] + shareDetails.height

                val rootLocation = IntArray(2)
                (root as View).getLocationOnScreen(rootLocation)
                val rootTop = rootLocation[1]

                val relativeBottom = shareDetailsBottom - rootTop
                val margin24dp = resources.getDimensionPixelSize(R.dimen.margin24)
                val peekHeight = relativeBottom + margin24dp
                val behavior = BottomSheetBehavior.from(tracksBottomSheet)
                behavior.peekHeight = peekHeight

                behavior.maxHeight = resources.displayMetrics.heightPixels - peekHeight
            }

            if(currentPlaylist.tracksCount == 0) {
                binding.placeholderNothing.isVisible = true
                binding.placeholderNothingText.isVisible = true
            } else {
                binding.placeholderNothing.isVisible = false
                binding.placeholderNothingText.isVisible = false
            }

            playlistNameDetails.text = currentPlaylist.name
            PlaylistNameItemPlayer.text = currentPlaylist.name

            if(currentPlaylist.description.isNotEmpty()){
                playlistDescriptionDetails.isVisible = true
                playlistDescriptionDetails.text = currentPlaylist.description
            } else playlistDescriptionDetails.isVisible = false


            totalTracks.text = resources.getQuantityString(
                R.plurals.howManyTracks,
                currentPlaylist.tracksCount,
                currentPlaylist.tracksCount
            )
            SongsCountItemPlayer.text = totalTracks.text

            viewModel.playlistTracks.observe(viewLifecycleOwner) { tracks ->
                val totalDuration = tracks.sumOf { it.trackTimeMillis }
                totalTime.text = formatTotalDuration(totalDuration)
            }

            currentPlaylist.coverPath?.let { coverPath ->
                Glide.with(requireContext())
                    .load(coverPath)
                    .into(coverDetails)

                Glide.with(requireContext())
                    .load(coverPath)
                    .override(DpToPx.dpToPx(45F, requireContext()), DpToPx.dpToPx(45F, requireContext()))
                    .centerCrop()
                    .into(artworkUrl100Playlist)
            } ?: run {
                coverDetails.setImageResource(R.drawable.big_placeholder)
                artworkUrl100Playlist.setImageResource(R.drawable.placeholder)
            }

            yearDetails.text = SimpleDateFormat("yyyy", Locale.getDefault())
                .format(Date(currentPlaylist.creationDate))
        }
    }


    private fun setupClickListeners() {
        binding.backToolbar.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.moreDetails.setOnClickListener {
            binding.moreBottomSheet.isVisible = true
            binding.overlay.isVisible = true
            setupBottomSheet()
        }

        binding.deletePlaylistBtn.setOnClickListener {
            binding.moreBottomSheet.isVisible = false
            binding.overlay.isVisible = false
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Удалить плейлист")
                .setMessage("Хотите удалить плейлист?")
                .setNegativeButton("Нет") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Да") { dialog, which ->
                    viewModel.deletePlaylist(currentPlaylist.id)
                    findNavController().popBackStack()
                    dialog.dismiss()
                }
                .show()
        }
        binding.shareBtn.setOnClickListener {
            viewModel.playlistTracks.value?.let { tracks ->
                if (tracks.isEmpty()) {
                    showEmptyPlaylistMessage()
                } else {
                    sharePlaylist(currentPlaylist, tracks)
                }
            }
        }
        binding.shareDetails.setOnClickListener {
            viewModel.playlistTracks.value?.let { tracks ->
                if (tracks.isEmpty()) {
                    showEmptyPlaylistMessage()
                } else {
                    sharePlaylist(currentPlaylist, tracks)
                }
            }
        }

        binding.editPlaylistBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("playlist", currentPlaylist)
            }
            findNavController().navigate(
                R.id.action_playlistDetailsFragment_to_refactorPlaylistFragment,
                bundle
            )
        }
    }

    private fun sharePlaylist(playlist: Playlist, tracks: List<Track>) {
        val shareText = buildString {
            append(playlist.name)
            append("\n")

            playlist.description.takeIf { it.isNotBlank() }?.let {
                append(it)
                append("\n")
            }

            append(resources.getQuantityString(
                R.plurals.howManyTracks,
                tracks.size,
                tracks.size
            ))
            append("\n")

            tracks.forEachIndexed { index, track ->
                append("${index + 1}. ${track.artistName} - ${track.trackName} (${formatTrackTime(track.trackTimeMillis)})\n")
            }
        }

        val shareIntent = Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            },
            getString(R.string.share_playlist_title)
        )

        startActivity(shareIntent)
    }

    private fun formatTrackTime(millis: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis.toLong()) % 60
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }

    private fun showEmptyPlaylistMessage() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.share_playlist_title)
            .setMessage(R.string.empty_playlist_message)
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun formatTotalDuration(millis: Int): String {
        val totalMinutes = TimeUnit.MILLISECONDS.toMinutes(millis.toLong())
        return resources.getQuantityString(R.plurals.howManyMinutes, totalMinutes.toInt(), totalMinutes)
    }

    override fun onDestroyView() {
        backPressedCallback?.remove()
        backPressedCallback = null
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        arguments?.getSerializable("playlist")?.let {
            val newPlaylist = it as Playlist
            if (newPlaylist != currentPlaylist) {
                currentPlaylist = newPlaylist
                bindPlaylistData()
            }
        }
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
}