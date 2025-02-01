package com.example.playlist__maker.sharing.domain.interactor.impl

import com.example.playlist__maker.sharing.domain.repository.ExternalNavigator
import com.example.playlist__maker.sharing.domain.interactor.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
): SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareApp()
    }

    override fun userAgree() {
        externalNavigator.userAgree()
    }

    override fun supportHelp() {
        externalNavigator.supportHelp()
    }
}