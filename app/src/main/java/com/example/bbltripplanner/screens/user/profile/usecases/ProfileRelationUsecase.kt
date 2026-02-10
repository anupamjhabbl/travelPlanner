package com.example.bbltripplanner.screens.user.profile.usecases

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollow
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollowersData
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollowingData
import com.example.bbltripplanner.screens.user.profile.repositories.ProfileRelationRepository

class ProfileRelationUsecase(
    private val profileRelationRepository: ProfileRelationRepository
) {
    suspend fun getFollowers(userId: String): ProfileFollowersData? {
        return BaseResponse.processResponse { profileRelationRepository.getFollowers(userId) }
    }

    suspend fun getFollowings(userId: String): ProfileFollowingData? {
        return BaseResponse.processResponse { profileRelationRepository.getFollowings(userId) }
    }

    suspend fun followUser(profileFollow: ProfileFollow) {
        profileRelationRepository.followUser(profileFollow)
    }

    suspend fun unfollowUser(profileFollow: ProfileFollow) {
        profileRelationRepository.unfollowUser(profileFollow)
    }

    suspend fun checkDoFollow(userId: String) {
        profileRelationRepository.checkDoFollow(userId)
    }
}