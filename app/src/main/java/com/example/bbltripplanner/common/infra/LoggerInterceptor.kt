package com.example.bbltripplanner.common.infra

import android.util.Log
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.utils.RequestUtils.toCurl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class LoggerInterceptor: Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d(Constants.TRIP_PLANNER_LOG_KEY, chain.request().toCurl())
        return chain.proceed(chain.request())
    }
}