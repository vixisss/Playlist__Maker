package com.example.playlist__maker.media.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.FragmentCreatePlaylistBinding

class CreatePlaylistFragment : Fragment() {
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

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

        exit()
        setupTextWatchers()
        updateInitialState()
    }

    private fun exit() {
        binding.toolbarCreatePlayList.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) = updateBorder()
        }

        binding.editTextTitle.addTextChangedListener(textWatcher)
        binding.editTextMessage.addTextChangedListener(textWatcher)

    }

    private fun updateInitialState() {
        with(binding) {
            editTextTitle.background = chooseBorderColor(editTextTitle.text.isNullOrEmpty())
            editTextMessage.background = chooseBorderColor(editTextMessage.text.isNullOrEmpty())
            updateButtonState()
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

            updateButtonState()
        }
    }

    private fun chooseBorderColor(isEmpty: Boolean): android.graphics.drawable.Drawable? {
        return ContextCompat.getDrawable(
            requireContext(),
            if (isEmpty) R.drawable.border_edittext_playlist
            else R.drawable.border_edittext_playlist_color
        )
    }

    private fun updateButtonState() {
        val isButtonEnabled = !binding.editTextTitle.text.isNullOrEmpty()

        binding.createPlaylistTV.apply {
            setBackgroundResource(
                if (isButtonEnabled) R.drawable.btn_create_playlist_enable
                else R.drawable.btn_create_playlist
            )
            isEnabled = isButtonEnabled
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}