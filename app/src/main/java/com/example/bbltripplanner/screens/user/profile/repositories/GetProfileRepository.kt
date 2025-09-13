package com.example.bbltripplanner.screens.user.profile.repositories

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.User

interface GetProfileRepository {
    suspend fun getUser(userId: String): BaseResponse<User>
    suspend fun followUser(userId: String): BaseResponse<String>
    suspend fun blockUser(userId: String): BaseResponse<String>
}