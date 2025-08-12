package com.example.bbltripplanner.screens.user.profile.repositoryImpl

import com.example.bbltripplanner.common.entity.ApiFailureException
import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.screens.user.profile.clients.UserClient
import com.example.bbltripplanner.screens.user.profile.repositories.GetProfileRepository

class GetProfileNetwork(
    private val userClient: UserClient
): GetProfileRepository {
    override suspend fun getUser(): User {
        return processGetUserResponse(userClient.getUser())
    }

    private fun processGetUserResponse(userResponse: BaseResponse<User>): User {
        if (!userResponse.isSuccess || userResponse.data == null) {
            throw ApiFailureException("No failure but still not getting data")
        } else {
            return userResponse.data
        }
    }
}