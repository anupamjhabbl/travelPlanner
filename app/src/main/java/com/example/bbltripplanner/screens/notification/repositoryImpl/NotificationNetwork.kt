package com.example.bbltripplanner.screens.notification.repositoryImpl

import com.example.bbltripplanner.screens.notification.client.NotificationClient
import com.example.bbltripplanner.screens.notification.model.NotificationModel
import com.example.bbltripplanner.screens.notification.repositories.NotificationRepository

class NotificationNetwork(
    private val notificationClient: NotificationClient
): NotificationRepository {
    override suspend fun fetchNotification(): List<NotificationModel> {
        return listOf(
            NotificationModel(
                id = "1",
                heading = "Payment Received",
                message = "You received ₹1,200 from Rahul for the Goa trip expenses.",
                iconUrl = "https://picsum.photos/400/200?1",
                date = System.currentTimeMillis() - 5 * 60 * 1000,
                isRead = false
            ),
            NotificationModel(
                id = "2",
                heading = "Reminder",
                message = "Don’t forget to settle your pending balance with Aman.",
                iconUrl = "https://picsum.photos/400/200?1",
                date = System.currentTimeMillis() - 60 * 60 * 1000,
                isRead = true
            ),
            NotificationModel(
                id = "3",
                heading = "Trip Update",
                message = "Your Manali trip budget has been updated by Riya.",
                iconUrl = "https://picsum.photos/400/200?1",
                date = System.currentTimeMillis() - 3 * 60 * 60 * 1000,
                isRead = false
            ),
            NotificationModel(
                id = "4",
                heading = "New Expense Added",
                message = "Cab expense of ₹450 added to Delhi trip.",
                iconUrl = "https://picsum.photos/400/200?1",
                date = System.currentTimeMillis() - 24 * 60 * 60 * 1000,
                isRead = true
            ),
            NotificationModel(
                id = "5",
                heading = "Settlement Done",
                message = "You settled ₹2,000 with Karan successfully.",
                iconUrl = "https://picsum.photos/400/200?1",
                date = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000,
                isRead = true
            ),
            NotificationModel(
                id = "6",
                heading = "New Member Joined",
                message = "Priya joined your Bangalore trip.",
                iconUrl = "https://picsum.photos/400/200?1",
                date = System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000,
                isRead = false
            ),
            NotificationModel(
                id = "7",
                heading = "Payment Request",
                message = "Saurabh requested ₹800 for dinner split.",
                iconUrl = "https://picsum.photos/400/200?1",
                date = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000,
                isRead = true
            ),
            NotificationModel(
                id = "8",
                heading = "Trip Closed",
                message = "Your Jaipur trip has been marked as completed.",
                iconUrl = "https://picsum.photos/400/200?1",
                date = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000,
                isRead = false
            )
        )
    }
}