package com.example.bbltripplanner.common.infra

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.example.bbltripplanner.common.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor(
    val context: Context
): Interceptor {
    @SuppressLint("HardwareIds")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        val newRequest: Request = request.newBuilder()
            .addHeader(Constants.HTTPHeaders.DEVICE_ID, "$deviceId")
            .build()
        return chain.proceed(newRequest)
    }
}