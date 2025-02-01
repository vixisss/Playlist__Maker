package com.example.playlist__maker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.playlist__maker.databinding.ActivitySettingsBinding
import com.example.playlist__maker.settings.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()
    private val themeObserver = Observer<Boolean> { darkThemeIsChecked->
        binding.switchTheme.isChecked = darkThemeIsChecked
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backToolbar.setOnClickListener {
            finish()
        }

        viewModel.getTheme().observe(this, themeObserver)
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