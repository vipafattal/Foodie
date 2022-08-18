package com.abedfattal.foodie

import android.app.Application
import com.abedfattal.foodie.framework.di.KoinService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FoodieApplication : Application() {

    companion object {
        lateinit var foodieApplication: FoodieApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        foodieApplication = this

        startKoin {
            androidLogger()
            androidContext(this@FoodieApplication)
            modules(KoinService.modules)
        }
    }
}