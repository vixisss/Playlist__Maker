package com.example.playlist__maker.sharing.domain.impl

import com.example.playlist__maker.sharing.domain.ExternalNavigator
import com.example.playlist__maker.sharing.domain.SharingInteractor

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