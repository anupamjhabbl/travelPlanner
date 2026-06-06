package com.example.bbltripplanner.common.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import androidx.core.net.toUri

object ImageActionUtils {

    fun downloadImage(context: Context, url: String, fileName: String) {
        if (url.startsWith("http://", ignoreCase = true) || url.startsWith("https://", ignoreCase = true)) {
            val request = DownloadManager.Request(url.toUri())
                .setTitle(fileName)
                .setDescription("Downloading image...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
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
