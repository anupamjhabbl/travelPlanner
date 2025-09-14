package com.example.bbltripplanner.screens.user.auth.repositories

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.screens.user.auth.entity.AuthToken
import com.example.bbltripplanner.screens.user.auth.entity.PasswordResetResponse
import com.example.bbltripplanner.screens.user.auth.entity.UserForgetPasswordBody
import com.example.bbltripplanner.screens.user.auth.entity.UserLoginBody
import com.example.bbltripplanner.screens.user.auth.entity.UserOTPVerifyBody
import com.example.bbltripplanner.screens.user.auth.entity.UserRegistrationBody
import com.example.bbltripplanner.screens.user.auth.entity.UserPasswordResetBody
import com.example.bbltripplanner.screens.user.auth.entity.UserRegisteredId
import retrofit2.Response
import retrofit2.Call

interface UserAuthRepository {
    suspend fun registerUser(userRegistrationBody: UserRegistrationBody): Response<BaseResponse<UserRegisteredId>>
    suspend fun loginUser(userLoginBody: UserLoginBody): Response<BaseResponse<AuthToken>>
    suspend fun verifyOTP(userOTPVerifyBody: UserOTPVerifyBody, origin: String): Response<BaseResponse<AuthToken>>
    suspend fun forgetPasswordRequestOTP(userForgetPasswordBody: UserForgetPasswordBody): Response<BaseResponse<UserRegisteredId>>
    suspend fun resetPassword(userResetBody: UserPasswordResetBody, accessToken: String): Response<BaseResponse<PasswordResetResponse>>
    fun getNewAccessToken(refreshToken: String): Call<BaseResponse<AuthToken>>
}