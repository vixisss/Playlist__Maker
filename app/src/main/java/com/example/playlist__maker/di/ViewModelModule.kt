package com.example.playlist__maker.di

import com.example.playlist__maker.player.ui.viewModel.PlayerViewModel
import com.example.playlist__maker.search.ui.viewModel.SearchViewModel
import com.example.playlist__maker.settings.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        PlayerViewModel(get())
    }

    viewModel{
        SearchViewModel(get(), get())
    }

    viewModel{
        SettingsViewModel(get(), get())
    }
}