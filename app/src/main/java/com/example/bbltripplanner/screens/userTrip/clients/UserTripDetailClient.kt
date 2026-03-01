package com.example.bbltripplanner.screens.userTrip.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import retrofit2.http.GET
import retrofit2.http.Path

interface UserTripDetailClient {
    @GET("getTrip/{tripId}")
    suspend fun getUserTripDetail(@Path("tripId") tripId: String): BaseResponse<TripData>
}