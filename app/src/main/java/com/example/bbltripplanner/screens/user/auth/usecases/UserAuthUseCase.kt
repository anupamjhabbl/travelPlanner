package com.example.bbltripplanner.screens.user.auth.usecases

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.User
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

class UserAuthUseCase(
    private val userAuthRepository: UserAuthRepository
) {
    suspend fun registerUser(userRegistrationBody: UserRegistrationBody): UserRegisteredId? {
        return BaseResponse.processResponse { userAuthRepository.registerUser(userRegistrationBody) }
    }

    suspend fun loginUser(userLoginBody: UserLoginBody): AuthToken? {
        return BaseResponse.processResponse { userAuthRepository.loginUser(userLoginBody) }
    }

    suspend fun verifyOTP(userOTPVerifyBody: UserOTPVerifyBody, origin: String): AuthToken? {
        return BaseResponse.processResponse { userAuthRepository.verifyOTP(userOTPVerifyBody, origin) }
    }

    suspend fun forgetPasswordRequestOTP(userForgetPasswordBody: UserForgetPasswordBody): UserRegisteredId? {
       return BaseResponse.processResponse { userAuthRepository.forgetPasswordRequestOTP(userForgetPasswordBody) }
    }

    suspend fun resetPassword(userResetBody: UserPasswordResetBody, accessToken: String): PasswordResetResponse? {
        return BaseResponse.processResponse { userAuthRepository.resetPassword(userResetBody, accessToken) }
    }

    fun getNewAccessToken(refreshToken: String): Call<BaseResponse<AuthToken>> {
        return userAuthRepository.getNewAccessToken(refreshToken)
    }
}