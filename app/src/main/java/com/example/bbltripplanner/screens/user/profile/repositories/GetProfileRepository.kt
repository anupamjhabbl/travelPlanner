package com.example.bbltripplanner.screens.user.profile.repositories

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.User
import okhttp3.MultipartBody

interface GetProfileRepository {
    suspend fun getUser(userId: String): BaseResponse<User>
    suspend fun followUser(userId: String): BaseResponse<String>
    suspend fun blockUser(userId: String): BaseResponse<String>
    suspend fun updateUser(profilePic: MultipartBody.Part?, name: String?, bio: String?, phone: String?): BaseResponse<User>
}