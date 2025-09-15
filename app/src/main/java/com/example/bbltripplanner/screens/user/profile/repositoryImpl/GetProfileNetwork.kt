package com.example.bbltripplanner.screens.user.profile.repositoryImpl

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.screens.user.profile.clients.UserClient
import com.example.bbltripplanner.screens.user.profile.repositories.GetProfileRepository
import okhttp3.MultipartBody
import retrofit2.Response

class GetProfileNetwork(
    private val userClient: UserClient
): GetProfileRepository {
    override suspend fun getUser(userId: String): Response<BaseResponse<User>> {
        return userClient.getUser(userId)
    }

    override suspend fun followUser(userId: String): Response<BaseResponse<String>> {
        return userClient.followUser(userId)
    }

    override suspend fun blockUser(userId: String): Response<BaseResponse<String>> {
        return userClient.blockUser(userId)
    }

    override suspend fun getLocalUser(): Response<BaseResponse<User>> {
        return userClient.getLocalUser()
    }

    override suspend fun updateUser(profilePic: MultipartBody.Part?, name: String?, bio: String?, phone: String?): Response<BaseResponse<User>> {
        return userClient.updateUser(profilePic, name, bio, phone)
    }

    override suspend fun logoutUser(): Response<BaseResponse<String>> {
        return userClient.logoutUser()
    }
}
