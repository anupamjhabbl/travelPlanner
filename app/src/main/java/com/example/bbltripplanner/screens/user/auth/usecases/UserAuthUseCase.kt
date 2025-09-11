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
        return userAuthRepository.registerUser(userRegistrationBody).processResponse()
    }

    suspend fun loginUser(userLoginBody: UserLoginBody): AuthToken? {
        return userAuthRepository.loginUser(userLoginBody).processResponse()
    }

    suspend fun verifyOTP(userOTPVerifyBody: UserOTPVerifyBody, origin: String): AuthToken? {
        return userAuthRepository.verifyOTP(userOTPVerifyBody, origin).processResponse()
    }

    suspend fun forgetPasswordRequestOTP(userForgetPasswordBody: UserForgetPasswordBody): UserRegisteredId? {
       return userAuthRepository.forgetPasswordRequestOTP(userForgetPasswordBody).processResponse()
    }

    suspend fun resetPassword(userResetBody: UserPasswordResetBody, accessToken: String): PasswordResetResponse? {
        return userAuthRepository.resetPassword(userResetBody, accessToken).processResponse()
    }

    suspend fun getLocalUser(): User? {
        return userAuthRepository.getLocalUser().processResponse()
    }

    fun getNewAccessToken(refreshToken: String): Call<BaseResponse<AuthToken>> {
        return userAuthRepository.getNewAccessToken(refreshToken)
    }
}