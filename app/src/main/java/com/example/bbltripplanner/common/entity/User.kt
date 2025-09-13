package com.example.bbltripplanner.common.entity

data class User(
    val id: String,
    val name: String,
    val phones: List<String> = emptyList(),
    val email: String?,
    val bio: String = "",
    val followCount: Long = 0,
    val followersCount: Long = 0,
    val tripCount: Long = 0,
    val createdAt: String,
    val updatedAt: String,
    val profilePicture: String,
    val userStory: String?
)