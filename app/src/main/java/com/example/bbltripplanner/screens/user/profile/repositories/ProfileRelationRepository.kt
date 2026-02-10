package com.example.bbltripplanner.screens.user.profile.repositories

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollow
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollowersData
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollowingData
import retrofit2.Response

interface ProfileRelationRepository {
    suspend fun getFollowers(userId: String): Response<BaseResponse<ProfileFollowersData>>
    suspend fun getFollowings(userId: String): Response<BaseResponse<ProfileFollowingData>>
    suspend fun followUser(profileFollow: ProfileFollow)
    suspend fun unfollowUser(profileFollow: ProfileFollow)
    suspend fun checkDoFollow(userId: String)
}