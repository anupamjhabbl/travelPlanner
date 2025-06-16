package com.example.bbltripplanner.application

import android.app.Application
import com.example.bbltripplanner.common.di_modules.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TripPlannerBaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TripPlannerBaseApplication)
            androidLogger()
            modules(appModule)
        }
    }
}