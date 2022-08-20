package com.abedfattal.foodie

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.abedfattal.foodie.framework.di.KoinService
import com.abedfattal.foodie.ui.preferences.UserPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FoodieApplication : Application() {

    companion object {
        lateinit var app: FoodieApplication
            private set

    }



    override fun onCreate() {
        super.onCreate()
        app = this
        startKoinService()

    }

    private fun startKoinService() {
        startKoin {
            androidLogger()
            androidContext(this@FoodieApplication)
            modules(KoinService.modules)
        }
    }

}