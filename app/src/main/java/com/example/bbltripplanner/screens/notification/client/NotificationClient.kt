package com.example.bbltripplanner.screens.notification.client

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.notification.model.NotificationModel
import retrofit2.http.GET

interface NotificationClient {
    @GET("/notification")
    suspend fun getNotification(): BaseResponse<List<NotificationModel>>
}