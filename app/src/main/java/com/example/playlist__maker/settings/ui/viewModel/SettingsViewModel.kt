package com.example.playlist__maker.settings.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist__maker.creator.Creator
import com.example.playlist__maker.settings.domain.interactor.AppSwitcherInteractor
import com.example.playlist__maker.sharing.domain.SharingInteractor


class SettingsViewModel(
    private val settingsInteractor: AppSwitcherInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun supportHelp() {
        sharingInteractor.supportHelp()
    }

    fun userAgree() {
        sharingInteractor.userAgree()
    }

    fun getTheme(): Boolean = settingsInteractor.getTheme()

    fun switchTheme(checked: Boolean) {
        settingsInteractor.switchTheme(checked)
    }

    companion object {
        fun factory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    settingsInteractor = Creator.provideAppSwitcherInteractor(),
                    sharingInteractor = Creator.provideSharingInteractor()
                )
            }
        }
    }
}