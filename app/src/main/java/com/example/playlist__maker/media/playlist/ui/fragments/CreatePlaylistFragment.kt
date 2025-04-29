package com.example.playlist__maker.media.playlist.ui.fragments

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.FragmentCreatePlaylistBinding
import com.example.playlist__maker.media.playlist.domain.models.Playlist
import com.example.playlist__maker.media.playlist.ui.viewModel.PlaylistViewModel
import com.example.playlist__maker.utils.DpToPx
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

open class CreatePlaylistFragment : Fragment() {
    protected var _binding: FragmentCreatePlaylistBinding? = null
    protected val binding get() = _binding!!
    protected var imageUri: Uri? = null
    open val viewModel by viewModel<PlaylistViewModel>()

    private val pickAlbumImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if(uri != null){
                imageUri = uri

                binding.setAlbumImage.isVisible = true
                binding.iconAddPhoto.isVisible = false

                Glide.with(this)
                    .load(imageUri)
                    .transform(RoundedCorners(DpToPx.dpToPx(8F, requireContext())))
                    .error(R.drawable.big_placeholder)
                    .into(binding.setAlbumImage)

                binding.setAlbumImage.setImageURI(uri)

                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                requireContext().contentResolver.takePersistableUriPermission(uri, flag)
            } else{
                binding.setAlbumImage.isVisible = false
                binding.iconAddPhoto.isVisible = true
            }

        }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextWatchers()
        updateInitialState()
        handleExit()
        setupListeners()
    }


    protected fun setupListeners(){
        binding.toolbarCreatePlayList.setOnClickListener {
            exit()
        }


        binding.iconAddPhoto.setOnClickListener {
            pickAlbumImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createPlaylistButton.setOnClickListener {
            createPlaylist()
        }
    }


    protected open fun createPlaylist() {
        val name = binding.editTextTitle.text.toString()
        if (name.isBlank()) return

        val description = binding.editTextMessage.text?.toString() ?: ""
        val coverUri = imageUri?.toString()

        val playlist = Playlist(
            name = name,
            description = description,
            coverPath = coverUri,
            tracksIdJson = "[]",
            tracksCount = 0
        )

        viewModel.createPlaylist(playlist)
        Toast.makeText(requireContext(),"Плейлист успешно создан!", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }


    protected open fun exit(){
        if(binding.editTextTitle.text.isNotEmpty() || binding.editTextMessage.text.isNotEmpty() || imageUri != null) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setNegativeButton("Отмена") { dialog, which ->
                    dialog.dismiss()
                }
                    .setPositiveButton("Завершить") { dialog, which ->
                        findNavController().popBackStack()
                    }
                    .show()
            } else findNavController().popBackStack()
    }


    private fun handleExit(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exit()
            }
        })
    }


    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {updateBorder()}

        }
        binding.editTextTitle.addTextChangedListener(textWatcher)
        binding.editTextMessage.addTextChangedListener(textWatcher)
    }


    private fun updateInitialState() {
        with(binding) {
            editTextTitle.background = chooseBorderColor(editTextTitle.text.isNullOrEmpty())
            editTextMessage.background = chooseBorderColor(editTextMessage.text.isNullOrEmpty())
            isButtonEnable()
        }
    }


    private fun updateBorder() {
        with(binding) {
            val hasTitleText = !editTextTitle.text.isNullOrEmpty()
            val hasMessageText = !editTextMessage.text.isNullOrEmpty()

            editTextTitle.background = chooseBorderColor(!hasTitleText)
            editTextMessage.background = chooseBorderColor(!hasMessageText)

            borderTitle.isVisible = hasTitleText
            borderMessage.isVisible = hasMessageText

            isButtonEnable()
        }
    }


    private fun chooseBorderColor(isEmpty: Boolean): Drawable? {
        return ContextCompat.getDrawable(
            requireContext(),
            if (isEmpty) R.drawable.border_edittext_playlist
            else R.drawable.border_edittext_playlist_color
        )
    }


    private fun isButtonEnable(){
        binding.createPlaylistButton.isEnabled = !binding.editTextTitle.text.isNullOrBlank()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        binding.editTextTitle.clearFocus()
        binding.editTextMessage.clearFocus()
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}