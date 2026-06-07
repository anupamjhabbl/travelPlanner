package com.example.bbltripplanner.screens.userTrip.workers

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.screens.userTrip.clients.FileUploadClient
import com.example.bbltripplanner.screens.userTrip.clients.TripGalleryClient
import com.example.bbltripplanner.screens.userTrip.entity.*
import com.example.bbltripplanner.screens.userTrip.repositories.TripGalleryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class TripGalleryUploadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val repository: TripGalleryRepository by inject()
    private val client: TripGalleryClient by inject()
    private val fileUploadClient: FileUploadClient by inject()

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return createForegroundInfo(0, 0)
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val photoIds = inputData.getLongArray(EXTRA_PHOTO_IDS) ?: return@withContext Result.failure()
        val tripId = inputData.getString(EXTRA_TRIP_ID) ?: return@withContext Result.failure()
        
        val photos = repository.getPhotosByIds(photoIds.toList())
        if (photos.isEmpty()) return@withContext Result.success()

        setForeground(createForegroundInfo(0, photos.size))

        val firstPhoto = photos.first()
        val request = PresignedUrlRequest(
            visibility = firstPhoto.visibility,
            selectedUserIds = firstPhoto.selectedUserIds,
            files = photos.map {
                PresignedUrlFile(
                    fileName = it.fileName ?: "image_${it.id}.jpg",
                    mimeType = it.mimeType ?: "image/jpeg",
                    fileSize = it.fileSize ?: 0L,
                    createdAt = it.createdAt
                )
            }
        )

        try {
            val response = client.getPresignedUrls(tripId, request)
            if (response.isSuccessful) {
                val presignedData = response.body()?.data ?: return@withContext Result.failure()
                
                var uploadedCount = 0
                val totalCount = photos.size

                photos.forEachIndexed { index, photo ->
                    val presignedInfo = presignedData.getOrNull(index) ?: return@forEachIndexed
                    
                    val file = File(photo.localPath)
                    if (file.exists()) {
                        updateForeground(uploadedCount, totalCount)
                        
                        val requestBody = file.asRequestBody(photo.mimeType?.toMediaTypeOrNull())
                        val uploadResponse = fileUploadClient.uploadFile(
                            url = presignedInfo.uploadUrl,
                            file = requestBody,
                            contentType = photo.mimeType ?: "image/jpeg"
                        )

                        if (uploadResponse.isSuccessful) {
                            repository.updatePhoto(photo.copy(uploadStatus = PhotoUploadStatus.COMPLETE))
                            file.delete()
                            uploadedCount++
                        } else {
                            repository.updatePhoto(photo.copy(uploadStatus = PhotoUploadStatus.FAILED))
                        }
                    } else {
                        repository.deletePhoto(photo.id)
                    }
                    updateForeground(uploadedCount, totalCount)
                }
                return@withContext Result.success()
            } else {
                return@withContext Result.retry()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext Result.retry()
        }
    }

    private fun createForegroundInfo(current: Int, total: Int): ForegroundInfo {
        val contentText = if (total > 0) {
            applicationContext.getString(R.string.notification_trip_gallery_upload_progress, current, total)
        } else {
            applicationContext.getString(R.string.notification_trip_gallery_upload_preparing)
        }
        
        val notification = NotificationCompat.Builder(applicationContext, Constants.Notification.TRIP_GALLERY_UPLOAD_CHANNEL_ID)
            .setContentTitle(applicationContext.getString(R.string.notification_trip_gallery_upload_title))
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_upload)
            .setProgress(total, current, total == 0)
            .setOngoing(true)
            .build()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                Constants.Notification.TRIP_GALLERY_UPLOAD_NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(Constants.Notification.TRIP_GALLERY_UPLOAD_NOTIFICATION_ID, notification)
        }
    }

    private suspend fun updateForeground(current: Int, total: Int) {
        setForeground(createForegroundInfo(current, total))
    }

    companion object {
        const val EXTRA_PHOTO_IDS = "extra_photo_ids"
        const val EXTRA_TRIP_ID = "extra_trip_id"

        fun enqueue(context: Context, photoIds: LongArray, tripId: String) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val data = Data.Builder()
                .putLongArray(EXTRA_PHOTO_IDS, photoIds)
                .putString(EXTRA_TRIP_ID, tripId)
                .build()

            val uploadWorkRequest = OneTimeWorkRequestBuilder<TripGalleryUploadWorker>()
                .setConstraints(constraints)
                .setInputData(data)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "trip_photo_upload_${System.currentTimeMillis()}",
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                uploadWorkRequest
            )
        }
    }
}
