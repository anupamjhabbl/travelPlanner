package com.example.bbltripplanner.common.infra

import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(
    private val authPreferencesUseCase: AuthPreferencesUseCase
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val accessToken: String = authPreferencesUseCase.getAccessToken()

        val newRequest: Request = request.newBuilder()
            .addHeader(Constants.HTTPHeaders.AUTHORIZATION, "${Constants.HTTPHeaders.AUTHORIZATION_BEARER} $accessToken")
            .build()
        return chain.proceed(newRequest)
    }
}
