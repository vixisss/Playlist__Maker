package com.example.playlist__maker.settings.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist__maker.settings.domain.interactor.AppSwitcherInteractor
import com.example.playlist__maker.sharing.domain.interactor.SharingInteractor


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

    fun getTheme(): LiveData<Boolean> = MutableLiveData(settingsInteractor.getTheme())

    fun switchTheme(checked: Boolean) {
        settingsInteractor.switchTheme(checked)
    }
}