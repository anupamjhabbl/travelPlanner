package com.example.bbltripplanner.screens.userTrip.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripPhotoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TripGalleryClient {
    @GET("trips/{tripId}/media")
    suspend fun getPhotos(@Path("tripId") tripId: String): Response<BaseResponse<List<TripPhotoResponse>?>>

    @GET("trips/photos/upload-url")
    suspend fun getPresignedUrl(@Query("fileName") fileName: String): String
}
