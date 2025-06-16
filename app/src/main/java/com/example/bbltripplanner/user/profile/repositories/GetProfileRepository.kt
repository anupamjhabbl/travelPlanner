package com.example.bbltripplanner.user.profile.repositories

import com.example.bbltripplanner.common.entity.User

interface GetProfileRepository {
    suspend fun getUser(): User
}