package com.example.bbltripplanner.common.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object ImageActionUtils {
    suspend fun downloadImage(context: Context, url: String, fileName: String) {
        if (url.startsWith("http://", ignoreCase = true) || url.startsWith("https://", ignoreCase = true)) {
            downloadImageToGallery(context, url, fileName)
        } else {
            try {
                val sourceFile = File(url)
                if (sourceFile.exists()) {
                    val publicDownloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val destinationFile = File(publicDownloadsDir, fileName)
                    FileInputStream(sourceFile).use { input ->
                        FileOutputStream(destinationFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    android.media.MediaScannerConnection.scanFile(
                        context,
                        arrayOf(destinationFile.absolutePath),
                        null,
                        null
                    )
                } else {
                    Log.e("DownloadImage", "Local source file does not exist at: $url")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("DownloadImage", "Failed to save local file to Downloads: ${e.message}")
            }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun downloadImageToGallery(
        context: Context,
        imageUrl: String,
        fileName: String
    ): Uri? = withContext(Dispatchers.IO) {

        val notificationId = System.currentTimeMillis().toInt()
        val notificationManager = NotificationManagerCompat.from(context)

        val builder = NotificationCompat.Builder(context, Constants.Notification.TRIP_MEDIA_DOWNLOAD_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle(context.getString(R.string.download_img_msg))
            .setContentText(fileName)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setProgress(100, 0, false)

        if (canPostNotifications(context)) {
            notificationManager.notify(notificationId, builder.build())
        }

        try {
            val connection = URL(imageUrl).openConnection() as HttpURLConnection
            connection.connect()

            val bitmap = BitmapFactory.decodeStream(connection.inputStream)
                ?: throw IllegalStateException("Failed to decode image")

            if (canPostNotifications(context)) {
                notificationManager.notify(
                    notificationId,
                    builder.setProgress(100, 50, false).setContentText("50%").build()
                )
            }

            val now = System.currentTimeMillis()
            val nowSeconds = now / 1000

            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_ADDED, nowSeconds)
                put(MediaStore.Images.Media.DATE_MODIFIED, nowSeconds)
                put(MediaStore.Images.Media.DATE_TAKEN, now)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/MyAppImages")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }

            val resolver = context.contentResolver
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: throw IllegalStateException("Failed to create MediaStore entry")

            resolver.openOutputStream(imageUri)?.use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
                output.flush()
            }
            bitmap.recycle()

            val completedValues = ContentValues().apply {
                put(MediaStore.Images.Media.DATE_ADDED, nowSeconds)
                put(MediaStore.Images.Media.DATE_MODIFIED, nowSeconds)
                put(MediaStore.Images.Media.DATE_TAKEN, now)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.IS_PENDING, 0)
                }
            }
            resolver.update(imageUri, completedValues, null, null)

            if (canPostNotifications(context)) {
                notificationManager.notify(
                    notificationId,
                    builder
                        .setOngoing(false)
                        .setSmallIcon(android.R.drawable.stat_sys_download_done)
                        .setContentTitle("Download complete")
                        .setContentText(fileName)
                        .setProgress(0, 0, false)
                        .build()
                )
            }

            imageUri

        } catch (e: Exception) {
            if (canPostNotifications(context)) {
                notificationManager.notify(
                    notificationId,
                    builder
                        .setOngoing(false)
                        .setContentTitle("Download failed")
                        .setContentText(e.message)
                        .setProgress(0, 0, false)
                        .build()
                )
            }
            null
        }
    }

    private fun canPostNotifications(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    suspend fun shareImage(context: Context, url: String) {
        withContext(Dispatchers.IO) {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false)
                .build()

            val result = loader.execute(request)
            if (result is SuccessResult) {
                val bitmap = (result.drawable as? BitmapDrawable)?.bitmap
                if (bitmap != null) {
                    val cachePath = File(context.cacheDir, "images")
                    cachePath.mkdirs()
                    val file = File(cachePath, "shared_image.png")
                    val stream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    stream.close()

                    val contentUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

                    if (contentUri != null) {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            setDataAndType(contentUri, context.contentResolver.getType(contentUri))
                            putExtra(Intent.EXTRA_STREAM, contentUri)
                            type = "image/png"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
                    }
                }
            }
        }
    }
}
