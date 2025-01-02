package com.example.bbltripplanner.user

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.User
import retrofit2.http.GET

interface UserClient {
    @GET("c78ea7c4-f499-4ce6-93ff-cfc26e854c71")
    suspend fun getUser(): BaseResponse<User>
}