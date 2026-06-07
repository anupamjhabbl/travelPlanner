package com.example.bbltripplanner.screens.userTrip.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.PresignedUrlRequest
import com.example.bbltripplanner.screens.userTrip.entity.PresignedUrlResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripPhotoResponse
import retrofit2.Response
import retrofit2.http.*

interface TripGalleryClient {
    @GET("trips/{tripId}/media")
    suspend fun getPhotos(@Path("tripId") tripId: String): Response<BaseResponse<List<TripPhotoResponse>?>>

    @POST("trips/{tripId}/media")
    suspend fun getPresignedUrls(@Path("tripId") tripId: String, @Body request: PresignedUrlRequest): Response<BaseResponse<List<PresignedUrlResponse>>>
}
