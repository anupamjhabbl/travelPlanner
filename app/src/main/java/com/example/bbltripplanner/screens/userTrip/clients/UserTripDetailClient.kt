package com.example.bbltripplanner.screens.userTrip.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import retrofit2.http.GET
import retrofit2.http.Query

interface UserTripDetailClient {
    @GET("tripDetail")
    suspend fun getUserTripDetail(@Query("tripId") tripId: String): BaseResponse<TripData>
}