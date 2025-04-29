package com.example.playlist__maker.media.playlist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.FragmentCreatePlaylistBinding
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import com.example.playlist__maker.media.playlist.ui.viewModel.RefactorPlaylistViewModel
import com.example.playlist__maker.utils.DpToPx
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class RefactorPlaylistFragment : CreatePlaylistFragment() {
    override val viewModel: RefactorPlaylistViewModel by viewModel()
    private lateinit var currentPlaylist: Playlist

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getSerializable("playlist")?.let {
            currentPlaylist = it as Playlist
            loadPlaylistData()
        }

        setupUI()
        setupListeners()
    }

    override fun exit() {
        if (binding.editTextTitle.text.isNullOrEmpty()) {
            findNavController().popBackStack()
            return
        }

        if (hasChanges()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Завершить редактирование плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setNegativeButton("Отмена") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Завершить") { dialog, which ->
                    findNavController().popBackStack()
                }
                .show()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun hasChanges(): Boolean {
        return binding.editTextTitle.text.toString() != currentPlaylist.name ||
                binding.editTextMessage.text.toString() != (currentPlaylist.description ?: "") ||
                imageUri?.toString() != currentPlaylist.coverPath
    }


    private fun loadPlaylistData() {
        with(binding) {
            editTextTitle.setText(currentPlaylist.name)
            editTextMessage.setText(currentPlaylist.description)

            setAlbumImage.isVisible = true
            iconAddPhoto.isVisible = false
            groupFrameAddPhoto.isVisible = false

            Glide.with(this@RefactorPlaylistFragment)
                .load(currentPlaylist.coverPath?.toUri() ?: R.drawable.big_placeholder)
                .transform(RoundedCorners(DpToPx.dpToPx(8F, requireContext())))
                .error(R.drawable.big_placeholder)
                .into(setAlbumImage)

            currentPlaylist.coverPath?.let { uriString ->
                imageUri = uriString.toUri()
            }
        }
    }

    private fun setupUI() {
        binding.toolbarCreatePlayList.setTitle(R.string.refactor)
        binding.createPlaylistButton.text = "Сохранить"
        binding.iconAddPhoto.isVisible = true
    }

    override fun createPlaylist() {
        val name = binding.editTextTitle.text.toString()
        if (name.isBlank()) {
            findNavController().popBackStack()
            return
        }

        val description = binding.editTextMessage.text?.toString() ?: ""
        val coverUri = imageUri?.toString()

        val updatedPlaylist = currentPlaylist.copy(
            name = name,
            description = description,
            coverPath = coverUri
        )

        viewModel.updatePlaylist(updatedPlaylist).observe(viewLifecycleOwner) { success ->
            if (success) {
                val bundle = Bundle().apply {
                    putSerializable("playlist", updatedPlaylist)
                }

                findNavController().navigate(
                    R.id.action_refactorPlaylistFragment_to_playlistDetailsFragment,
                    bundle
                )

                Toast.makeText(requireContext(), "Плейлист обновлен!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}