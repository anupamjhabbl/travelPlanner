package com.example.bbltripplanner.screens.user.profile.repositoryImpl

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.screens.user.profile.clients.UserClient
import com.example.bbltripplanner.screens.user.profile.repositories.GetProfileRepository

class GetProfileNetwork(
    private val userClient: UserClient
): GetProfileRepository {
    override suspend fun getUser(userId: String): BaseResponse<User> {
        return userClient.getUser(userId)
    }

    override suspend fun followUser(userId: String): BaseResponse<String> {
        return userClient.followUser(userId)
    }

    override suspend fun blockUser(userId: String): BaseResponse<String> {
        return userClient.blockUser(userId)
    }
}