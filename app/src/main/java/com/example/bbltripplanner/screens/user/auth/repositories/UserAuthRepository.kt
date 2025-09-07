package com.example.bbltripplanner.screens.user.auth.repositories

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.user.auth.entity.AuthToken
import com.example.bbltripplanner.screens.user.auth.entity.PasswordResetResponse
import com.example.bbltripplanner.screens.user.auth.entity.UserForgetPasswordBody
import com.example.bbltripplanner.screens.user.auth.entity.UserLoginBody
import com.example.bbltripplanner.screens.user.auth.entity.UserOTPVerifyBody
import com.example.bbltripplanner.screens.user.auth.entity.UserRegistrationBody
import com.example.bbltripplanner.screens.user.auth.entity.UserPasswordResetBody
import com.example.bbltripplanner.screens.user.auth.entity.UserRegisteredId

interface UserAuthRepository {
    suspend fun registerUser(userRegistrationBody: UserRegistrationBody): BaseResponse<UserRegisteredId>
    suspend fun loginUser(userLoginBody: UserLoginBody): BaseResponse<AuthToken>
    suspend fun verifyOTP(userOTPVerifyBody: UserOTPVerifyBody): BaseResponse<AuthToken>
    suspend fun forgetPasswordRequestOTP(userForgetPasswordBody: UserForgetPasswordBody): BaseResponse<UserRegisteredId>
    suspend fun resetPassword(userResetBody: UserPasswordResetBody): BaseResponse<PasswordResetResponse>
}