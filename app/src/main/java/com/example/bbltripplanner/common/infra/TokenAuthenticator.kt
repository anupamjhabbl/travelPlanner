package com.example.bbltripplanner.common.infra

import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.user.auth.entity.AuthToken
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.auth.usecases.UserAuthUseCase
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException

class TokenAuthenticator(
    private val authPreferencesUseCase: AuthPreferencesUseCase,
    private val userAuthUseCase: UserAuthUseCase
) : Authenticator {
    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            if (responseCount(response) >= 2) {
                return null
            }

            val refreshToken = authPreferencesUseCase.getRefreshToken() ?: return null

            val refreshResponse = try {
                userAuthUseCase.getNewAccessToken(refreshToken).execute()
            } catch (e: Exception) {
                return null
            }

            if (refreshResponse.isSuccessful && refreshResponse.body()?.isSuccess == true) {
                val newAccessToken = refreshResponse.body()?.data?.accessToken
                if (!newAccessToken.isNullOrEmpty()) {
                    authPreferencesUseCase.saveAccessToken(newAccessToken)
                    return response.request.newBuilder()
                        .header(Constants.HTTPHeaders.AUTHORIZATION, "${Constants.HTTPHeaders.AUTHORIZATION_BEARER} $newAccessToken")
                        .build()
                }
                if (isaAuthInvalidError(refreshResponse)) {
                    removeTokens()
                }
                return null
            } else {
                if (isaAuthInvalidError(refreshResponse)) {
                    removeTokens()
                }
                return null
            }
        }
    }

    private fun isaAuthInvalidError(refreshResponse: retrofit2.Response<BaseResponse<AuthToken>>): Boolean {
        return refreshResponse.code() == 401 || refreshResponse.code() == 403
    }

    private fun removeTokens() {
        authPreferencesUseCase.removeAccessToken()
        authPreferencesUseCase.removeRefreshToken()
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            result++
            priorResponse = priorResponse.priorResponse
        }
        return result
    }
}