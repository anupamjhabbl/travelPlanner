package com.example.bbltripplanner.screens.notification.usecases

import com.example.bbltripplanner.screens.notification.model.NotificationModel
import com.example.bbltripplanner.screens.notification.repositories.NotificationRepository

class NotificationUseCase(
    private val notificationRepository: NotificationRepository
) {
    suspend fun getNotifications(): List<NotificationModel> {
        return notificationRepository.fetchNotification()
    }
}