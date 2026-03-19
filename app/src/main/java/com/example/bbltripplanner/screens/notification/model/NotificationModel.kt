package com.example.bbltripplanner.screens.notification.model

data class NotificationModel(
    val id: String,
    val heading: String,
    val message: String,
    val iconUrl: String? = null,
    val date: Long,
    val isRead: Boolean = false
)