package com.example.bbltripplanner.screens.user.profile.repositoryImpl

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.user.profile.clients.ProfileRelationClient
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollow
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollowersData
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollowingData
import com.example.bbltripplanner.screens.user.profile.repositories.ProfileRelationRepository
import retrofit2.Response

class ProfileRelationNetwork(
    private val profileRelationClient: ProfileRelationClient
): ProfileRelationRepository {
    override suspend fun getFollowers(userId: String): Response<BaseResponse<ProfileFollowersData>> {
        return profileRelationClient.getFollowers(userId)
    }

    override suspend fun getFollowings(userId: String): Response<BaseResponse<ProfileFollowingData>> {
        return profileRelationClient.getFollowings(userId)
    }

    override suspend fun followUser(profileFollow: ProfileFollow) {
        profileRelationClient.followUser(profileFollow)
    }

    override suspend fun unfollowUser(profileFollow: ProfileFollow) {
        profileRelationClient.unfollowUser(profileFollow)
    }

    override suspend fun checkDoFollow(userId: String) {
        profileRelationClient.checkDoFollow(userId)
    }
}