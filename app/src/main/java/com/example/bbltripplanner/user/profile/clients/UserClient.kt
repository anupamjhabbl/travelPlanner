package com.example.bbltripplanner.user.profile.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.User
import retrofit2.http.GET

interface UserClient {
    @GET("f7e92587-34f8-4a9c-9697-eb62844f4c4a")
    suspend fun getUser(userId: String): BaseResponse<User>

    @GET("f7e92587-34f8-4a9c-9697-eb62844f4c4a")
    suspend fun getUser(): BaseResponse<User>
}