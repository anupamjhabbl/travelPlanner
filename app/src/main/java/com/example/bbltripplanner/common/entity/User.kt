package com.example.bbltripplanner.common.entity

data class User(
    val id: String,
    val name: String,
    val phone: String?,
    val email: String?,
    val bio: String = "",
    val followCount: Long = 0,
    val followersCount: Long = 0,
    val tripCount: Long = 0,
    val profilePicture: String,
    val userStory: String?
)

data class UpdateUserData(
    val name: String? = null,
    val phone: String? = null,
    val bio: String? = null,
)