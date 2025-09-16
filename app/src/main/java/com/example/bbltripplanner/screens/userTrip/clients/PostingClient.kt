package com.example.bbltripplanner.screens.userTrip.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import retrofit2.http.Body
import retrofit2.http.POST

interface PostingClient {
    @POST("postTrip")
    suspend fun postTrip(@Body tripData: TripData): BaseResponse<TripData>
}