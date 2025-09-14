package com.example.bbltripplanner.screens.user.profile.usecases

import com.example.bbltripplanner.common.entity.UpdateUserData
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.screens.user.profile.repositories.GetProfileRepository
import okhttp3.MultipartBody

class ProfileUseCase(
    private val getProfileRepository: GetProfileRepository
) {
    suspend fun getUserProfile(userId: String): User? {
        return getProfileRepository.getUser(userId).processResponse()
    }

    suspend fun followUser(userId: String): String? {
        return getProfileRepository.followUser(userId).processResponse()
    }

    suspend fun blockUser(userId: String): String? {
        return getProfileRepository.blockUser(userId).processResponse()
    }

    suspend fun updateUser(user: UpdateUserData?, profilePic: MultipartBody.Part?): User? {
        return getProfileRepository.updateUser(profilePic, user?.name, user?.bio, user?.phone).processResponse()
    }
}