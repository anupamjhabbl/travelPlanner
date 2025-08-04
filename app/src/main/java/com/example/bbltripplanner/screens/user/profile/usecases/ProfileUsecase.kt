package com.example.bbltripplanner.screens.user.profile.usecases

import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.screens.user.profile.repositories.GetProfileRepository

class ProfileUseCase(
    private val getProfileRepository: GetProfileRepository
) {
    suspend fun getOwnProfile(): User {
        return getProfileRepository.getUser()
    }
}