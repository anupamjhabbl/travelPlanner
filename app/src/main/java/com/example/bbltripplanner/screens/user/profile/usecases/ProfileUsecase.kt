package com.example.bbltripplanner.screens.user.profile.usecases

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.UpdateUserData
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.screens.user.profile.repositories.GetProfileRepository
import okhttp3.MultipartBody

class ProfileUseCase(
    private val getProfileRepository: GetProfileRepository
) {
    suspend fun getUserProfile(userId: String): User? {
        return BaseResponse.processResponse { getProfileRepository.getUser(userId) }
    }

    suspend fun followUser(userId: String): String? {
        return BaseResponse.processResponse { getProfileRepository.followUser(userId) }
    }

    suspend fun blockUser(userId: String): String? {
        return BaseResponse.processResponse { getProfileRepository.blockUser(userId) }
    }

    suspend fun getLocalUser(): User? {
        return BaseResponse.processResponse { getProfileRepository.getLocalUser() }
    }

    suspend fun updateUser(user: UpdateUserData?, profilePic: MultipartBody.Part?): User? {
        return BaseResponse.processResponse { getProfileRepository.updateUser(profilePic, user?.name, user?.bio, user?.phone) }
    }

    suspend fun logoutUser(): String? {
        return BaseResponse.processResponse { getProfileRepository.logoutUser() }
    }
}