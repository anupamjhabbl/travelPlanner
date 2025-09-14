package com.example.bbltripplanner.screens.user.profile.repositories

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.User
import okhttp3.MultipartBody
import retrofit2.Response

interface GetProfileRepository {
    suspend fun getUser(userId: String): Response<BaseResponse<User?>>
    suspend fun followUser(userId: String): Response<BaseResponse<String>>
    suspend fun blockUser(userId: String): Response<BaseResponse<String>>
    suspend fun getLocalUser(): Response<BaseResponse<User>>
    suspend fun updateUser(profilePic: MultipartBody.Part?, name: String?, bio: String?, phone: String?): Response<BaseResponse<User>>
}