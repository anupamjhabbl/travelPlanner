package com.example.bbltripplanner.screens.user.profile.entity

import com.example.bbltripplanner.common.entity.User

data class ProfileFollowingData(
    val following: List<User> = emptyList()
)
