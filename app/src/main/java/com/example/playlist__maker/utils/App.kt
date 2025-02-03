package com.example.playlist__maker.utils

import android.app.Application
import com.example.playlist__maker.di.dataModule
import com.example.playlist__maker.di.interactorModule
import com.example.playlist__maker.di.repositoryModule
import com.example.playlist__maker.di.viewModelModule
import com.example.playlist__maker.settings.domain.interactor.AppSwitcherInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    private val themeUseCase: AppSwitcherInteractor by inject()

    companion object {
        const val APP_PREFERENCES = "app_preferences"
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
        themeUseCase.switchTheme(themeUseCase.getTheme())
    }
}