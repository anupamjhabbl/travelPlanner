package com.example.bbltripplanner.screens.user.profile.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserClient {
    @GET("/user/{userId}")
    suspend fun getUser(@Path("userId") userId: String): BaseResponse<User>

    @GET("/user/follow")
    suspend fun followUser(@Query("userId") userId: String): BaseResponse<String>

    @GET("/user/block")
    suspend fun blockUser(@Query("userId") userId: String): BaseResponse<String>
}