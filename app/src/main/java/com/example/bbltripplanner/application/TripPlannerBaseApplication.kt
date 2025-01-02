package com.example.bbltripplanner.application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class TripPlannerBaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TripPlannerBaseApplication)
            androidLogger()
            modules(com.example.bbltripplanner.common.di_modules.ApplicationModule().module)
        }
    }
}