package com.example.bbltripplanner.screens.user.auth.usecases

import com.example.bbltripplanner.screens.user.auth.entity.AuthToken
import com.example.bbltripplanner.screens.user.auth.entity.PasswordResetResponse
import com.example.bbltripplanner.screens.user.auth.entity.UserForgetPasswordBody
import com.example.bbltripplanner.screens.user.auth.entity.UserLoginBody
import com.example.bbltripplanner.screens.user.auth.entity.UserOTPVerifyBody
import com.example.bbltripplanner.screens.user.auth.entity.UserPasswordResetBody
import com.example.bbltripplanner.screens.user.auth.entity.UserRegisteredId
import com.example.bbltripplanner.screens.user.auth.entity.UserRegistrationBody
import com.example.bbltripplanner.screens.user.auth.repositories.UserAuthRepository

class UserAuthUseCase(
    private val userAuthRepository: UserAuthRepository
) {
    suspend fun registerUser(userRegistrationBody: UserRegistrationBody): UserRegisteredId {
        return userAuthRepository.registerUser(userRegistrationBody).processResponse()
    }

    suspend fun loginUser(userLoginBody: UserLoginBody): AuthToken {
        return userAuthRepository.loginUser(userLoginBody).processResponse()
    }

    suspend fun verifyOTP(userOTPVerifyBody: UserOTPVerifyBody): AuthToken {
        return userAuthRepository.verifyOTP(userOTPVerifyBody).processResponse()
    }

    suspend fun forgetPasswordRequestOTP(userForgetPasswordBody: UserForgetPasswordBody): UserRegisteredId {
       return userAuthRepository.forgetPasswordRequestOTP(userForgetPasswordBody).processResponse()
    }

    suspend fun resetPassword(userResetBody: UserPasswordResetBody): PasswordResetResponse {
        return userAuthRepository.resetPassword(userResetBody).processResponse()
    }
}