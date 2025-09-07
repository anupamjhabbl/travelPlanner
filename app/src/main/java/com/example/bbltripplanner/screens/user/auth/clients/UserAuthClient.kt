package com.example.bbltripplanner.screens.user.auth.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.user.auth.entity.AuthToken
import com.example.bbltripplanner.screens.user.auth.entity.PasswordResetResponse
import com.example.bbltripplanner.screens.user.auth.entity.UserForgetPasswordBody
import com.example.bbltripplanner.screens.user.auth.entity.UserLoginBody
import com.example.bbltripplanner.screens.user.auth.entity.UserOTPVerifyBody
import com.example.bbltripplanner.screens.user.auth.entity.UserPasswordResetBody
import com.example.bbltripplanner.screens.user.auth.entity.UserRegisteredId
import com.example.bbltripplanner.screens.user.auth.entity.UserRegistrationBody
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAuthClient {
    @POST("auth/register")
    suspend fun registerUser(@Body userRegistrationBody: UserRegistrationBody): BaseResponse<UserRegisteredId>

    @POST("auth/login")
    suspend fun loginUser(@Body userLoginBody: UserLoginBody): BaseResponse<AuthToken>

    @POST("auth/verifyOtp")
    suspend fun verifyOTP(@Body userOTPVerifyBody: UserOTPVerifyBody): BaseResponse<AuthToken>

    @POST("auth/forgetPassword")
    suspend fun forgetPasswordRequestOTP(@Body userForgetPasswordBody: UserForgetPasswordBody): BaseResponse<UserRegisteredId>

    @POST("auth/resetPassword")
    suspend fun resetPassword(@Body userResetBody: UserPasswordResetBody): BaseResponse<PasswordResetResponse>
}