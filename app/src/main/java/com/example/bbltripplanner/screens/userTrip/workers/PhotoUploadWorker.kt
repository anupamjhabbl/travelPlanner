package com.example.bbltripplanner.screens.userTrip.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bbltripplanner.screens.userTrip.repositories.TripGalleryRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PhotoUploadWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val repository: TripGalleryRepository by inject()

    override suspend fun doWork(): Result {
        val photoId = inputData.getLong("PHOTO_ID", -1)
        if (photoId == -1L) return Result.failure()

        return try {
            // 1. Get presigned URL from repository/client
            // 2. Upload file to S3
            // 3. Update status in local DB via repository
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
