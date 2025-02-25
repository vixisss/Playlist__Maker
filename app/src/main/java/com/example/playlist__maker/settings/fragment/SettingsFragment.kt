package com.example.playlist__maker.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.playlist__maker.databinding.FragmentSettingsBinding
import com.example.playlist__maker.settings.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingsViewModel>()

    private val themeObserver = Observer<Boolean> { isDarkTheme ->
        binding.switchTheme.setOnCheckedChangeListener(null)
        binding.switchTheme.isChecked = isDarkTheme
        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTheme().observe(viewLifecycleOwner, themeObserver)

        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }

        binding.btnShareApp.setOnClickListener {
            viewModel.shareApp()
        }
        binding.btnSupportHelp.setOnClickListener {
            viewModel.supportHelp()
        }
        binding.btnUserAgree.setOnClickListener {
            viewModel.userAgree()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}