package com.example.bbltripplanner.common.entity

data class User(
    val id: Long,
    val name: String,
    val contact: Contact,
    val bio: String = "",
    val followCount: Long = 0,
    val followersCount: Long = 0,
    val tripCount: Long = 0,
    val createdAt: String,
    val updatedAt: String,
    val userSettings: UserSettings,
    val profilePicture: String
)

data class Contact(
    val phones: List<String> = emptyList(),
    val email: String?
)

data class UserSettings(
    val themePreference: com.example.bbltripplanner.common.Constants.Theme
)