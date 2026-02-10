package com.example.bbltripplanner.screens.user.profile.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollow
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollowersData
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollowingData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileRelationClient {
    @GET("relationships/followers/{userId}")
    suspend fun getFollowers(@Path("userId") userId: String): Response<BaseResponse<ProfileFollowersData>>

    @GET("relationships/following/{userId}")
    suspend fun getFollowings(@Path("userId") userId: String): Response<BaseResponse<ProfileFollowingData>>

    @POST("relationships/follow")
    suspend fun followUser(@Body profileFollow: ProfileFollow)

    @POST("relationships/unfollow")
    suspend fun unfollowUser(@Body profileFollow: ProfileFollow)

    @GET("relationships/check")
    suspend fun checkDoFollow(@Query("followingId") followingId: String)
}