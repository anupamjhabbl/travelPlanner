package com.example.bbltripplanner.screens.user.profile.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


interface UserClient {
    @GET("user/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<BaseResponse<User>>

    @GET("user/block/{userId}")
    suspend fun blockUser(@Path("userId") userId: String): Response<BaseResponse<String>>

    @GET("user/unblock/{userId}")
    suspend fun unblockUser(@Path("userId") userId: String): Response<BaseResponse<String>>

    @GET("user/blocked-users")
    suspend fun getBlockedUsers(): Response<BaseResponse<List<User>>>

    @GET("user/me")
    suspend fun getLocalUser(): Response<BaseResponse<User>>

    @Multipart
    @PUT("user/update")
    suspend fun updateUser(@Part profilePic: MultipartBody.Part?, @Part("name") name: RequestBody?, @Part("bio") bio: RequestBody?, @Part("phone") phone: RequestBody?): Response<BaseResponse<User>>

    @POST("auth/logout")
    suspend fun logoutUser(): Response<BaseResponse<String>>
}