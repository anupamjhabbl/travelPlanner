package com.example.bbltripplanner.screens.notification.repositories

import com.example.bbltripplanner.screens.notification.model.NotificationModel

interface NotificationRepository {
    suspend fun fetchNotification(): List<NotificationModel>
}