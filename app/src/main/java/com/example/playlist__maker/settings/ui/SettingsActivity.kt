package com.example.playlist__maker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlist__maker.databinding.ActivitySettingsBinding
import com.example.playlist__maker.settings.ui.viewModel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, SettingsViewModel.factory())[SettingsViewModel::class.java]

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backToolbar.setOnClickListener {
            finish()
        }

        binding.switchTheme.isChecked = viewModel.getTheme()
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
}