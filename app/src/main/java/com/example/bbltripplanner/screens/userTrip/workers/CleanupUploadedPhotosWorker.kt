package com.example.bbltripplanner.screens.userTrip.workers

import android.content.Context
import androidx.work.*
import com.example.bbltripplanner.screens.userTrip.entity.PhotoUploadStatus
import com.example.bbltripplanner.screens.userTrip.repositories.TripGalleryRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.util.concurrent.TimeUnit

class CleanupUploadedPhotosWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val repository: TripGalleryRepository by inject()

    override suspend fun doWork(): Result {
        return try {
            val completedPhotos = repository.getPhotosByStatus(PhotoUploadStatus.COMPLETE)
            completedPhotos.forEach { photo ->
                val file = File(photo.localPath)
                if (file.exists()) {
                    file.delete()
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        private const val WORK_NAME = "cleanup_uploaded_photos_worker"

        fun schedule(context: Context) {
            val cleanupRequest = PeriodicWorkRequestBuilder<CleanupUploadedPhotosWorker>(
                1, TimeUnit.DAYS
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                cleanupRequest
            )
        }
    }
}
