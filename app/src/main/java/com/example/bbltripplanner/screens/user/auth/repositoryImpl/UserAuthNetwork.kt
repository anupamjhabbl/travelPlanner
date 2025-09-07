package com.example.bbltripplanner.screens.user.auth.repositoryImpl

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.user.auth.clients.UserAuthClient
import com.example.bbltripplanner.screens.user.auth.entity.AuthToken
import com.example.bbltripplanner.screens.user.auth.entity.PasswordResetResponse
import com.example.bbltripplanner.screens.user.auth.entity.UserForgetPasswordBody
import com.example.bbltripplanner.screens.user.auth.entity.UserLoginBody
import com.example.bbltripplanner.screens.user.auth.entity.UserOTPVerifyBody
import com.example.bbltripplanner.screens.user.auth.entity.UserPasswordResetBody
import com.example.bbltripplanner.screens.user.auth.entity.UserRegisteredId
import com.example.bbltripplanner.screens.user.auth.entity.UserRegistrationBody
import com.example.bbltripplanner.screens.user.auth.repositories.UserAuthRepository
import retrofit2.Call

class UserAuthNetwork(
    private val userAuthClient: UserAuthClient
): UserAuthRepository {
    override suspend fun registerUser(userRegistrationBody: UserRegistrationBody): BaseResponse<UserRegisteredId> {
        return userAuthClient.registerUser(userRegistrationBody)
    }

    override suspend fun loginUser(userLoginBody: UserLoginBody): BaseResponse<AuthToken> {
        return userAuthClient.loginUser(userLoginBody)
    }

    override suspend fun verifyOTP(userOTPVerifyBody: UserOTPVerifyBody, origin: String): BaseResponse<AuthToken> {
        return userAuthClient.verifyOTP(userOTPVerifyBody, origin)
    }

    override suspend fun forgetPasswordRequestOTP(userForgetPasswordBody: UserForgetPasswordBody): BaseResponse<UserRegisteredId> {
        return userAuthClient.forgetPasswordRequestOTP(userForgetPasswordBody)
    }

    override suspend fun resetPassword(userResetBody: UserPasswordResetBody, accessToken: String): BaseResponse<PasswordResetResponse> {
        return userAuthClient.resetPassword(userResetBody, accessToken)
    }

    override fun getNewAccessToken(refreshToken: String): Call<BaseResponse<AuthToken>> {
        return userAuthClient.getNewAccessToken(refreshToken)
    }
}