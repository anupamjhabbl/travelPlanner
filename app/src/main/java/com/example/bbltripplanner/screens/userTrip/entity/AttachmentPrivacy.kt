package com.example.bbltripplanner.screens.userTrip.entity

import kotlinx.serialization.SerialName

enum class AttachmentPrivacy(
    val value: String
) {
    @SerialName("PRIVATE")
    PRIVATE("Private"),
    @SerialName("PUBLIC")
    PUBLIC("Public"),
    @SerialName("SELECTED")
    SELECTED("Selected")
}