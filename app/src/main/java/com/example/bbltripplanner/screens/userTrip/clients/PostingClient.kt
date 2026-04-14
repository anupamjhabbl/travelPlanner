package com.example.bbltripplanner.screens.userTrip.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripCreationResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripDataRequestModel
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostingClient {
    @POST("trips/createTrip")
    suspend fun postTrip(@Body tripData: TripDataRequestModel): BaseResponse<TripCreationResponse>

    @PUT("trips/updateTrip/{tripId}")
    suspend fun updateTrip(@Path("tripId") tripId: String, @Body tripData: TripDataRequestModel): BaseResponse<String?>
}