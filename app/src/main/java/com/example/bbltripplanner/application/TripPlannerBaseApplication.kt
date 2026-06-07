package com.example.bbltripplanner.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.Constants.Notification.TRIP_MEDIA_DOWNLOAD_CHANNEL_NAME
import com.example.bbltripplanner.common.di_modules.appModule
import com.example.bbltripplanner.screens.userTrip.workers.CleanupUploadedPhotosWorker
import com.example.bbltripplanner.screens.userTrip.workers.RetryFailedUploadWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TripPlannerBaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        createUploadChannel()
        createDownloadChannel()

        startKoin {
            androidContext(this@TripPlannerBaseApplication)
            androidLogger()
            modules(appModule)
        }

        RetryFailedUploadWorker.schedule(this)
        CleanupUploadedPhotosWorker.schedule(this)
    }

    private fun createUploadChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.Notification.TRIP_GALLERY_UPLOAD_CHANNEL_ID,
                Constants.Notification.TRIP_GALLERY_UPLOAD_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.show_photo_upload_desc)
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun createDownloadChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.Notification.TRIP_MEDIA_DOWNLOAD_CHANNEL_ID,
                TRIP_MEDIA_DOWNLOAD_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.show_photo_download_desc)
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
