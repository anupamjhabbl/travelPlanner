package com.example.bbltripplanner.screens.userTrip.workers

import android.content.Context
import androidx.work.*
import com.example.bbltripplanner.screens.userTrip.entity.PhotoUploadStatus
import com.example.bbltripplanner.screens.userTrip.repositories.TripGalleryRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class RetryFailedUploadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {
    private val repository: TripGalleryRepository by inject()

    override suspend fun doWork(): Result {
        val failedPhotos = repository.getPhotosByStatus(PhotoUploadStatus.FAILED)
        if (failedPhotos.isEmpty()) return Result.success()

        val photosByTrip = failedPhotos.groupBy { it.tripId }

        photosByTrip.forEach { (tripId, photos) ->
            val photoIds = photos.map { it.id }.toLongArray()
            TripGalleryUploadWorker.enqueue(applicationContext, photoIds, tripId)
        }

        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "retry_failed_uploads_worker"

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()

            val retryRequest = PeriodicWorkRequestBuilder<RetryFailedUploadWorker>(
                1, TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                retryRequest
            )
        }
    }
}
