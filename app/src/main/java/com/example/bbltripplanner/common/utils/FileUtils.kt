package com.example.bbltripplanner.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    fun bitmapToFile(bitmap: Bitmap, context: Context): File {
        val file = File(context.cacheDir, "profile_pic_${System.currentTimeMillis()}.jpg")
        file.createNewFile()

        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos)
        val bitmapData = bos.toByteArray()

        FileOutputStream(file).use { fos ->
            fos.write(bitmapData)
            fos.flush()
        }

        return file
    }

    fun fileToMultipart(file: File, partName: String = "file"): MultipartBody.Part {
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }

    fun bitMapToMultipart(bitmap: Bitmap, context: Context, partName: String = "file"): MultipartBody.Part {
        return fileToMultipart(bitmapToFile(bitmap, context), partName)
    }

    fun uriToMultipart(context: Context, uri: Uri, partName: String = "file"): MultipartBody.Part? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null

            val file = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()

            fileToMultipart(file, partName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}