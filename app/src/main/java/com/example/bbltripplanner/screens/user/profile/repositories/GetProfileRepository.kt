package com.example.bbltripplanner.screens.user.profile.repositories

import com.example.bbltripplanner.common.entity.User

interface GetProfileRepository {
    suspend fun getUser(): User
}