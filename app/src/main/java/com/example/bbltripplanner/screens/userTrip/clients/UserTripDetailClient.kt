package com.example.bbltripplanner.screens.userTrip.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.entity.TripMember
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserTripDetailClient {
    @GET("trips/getTrip/{tripId}")
    suspend fun getUserTripDetail(@Path("tripId") tripId: String): BaseResponse<TripData>

    @PUT("trips/acceptInvitation/{tripId}")
    suspend fun acceptInvitation(@Path("tripId") tripId: String): BaseResponse<Boolean>

    @GET("trips/{tripId}/members")
    suspend fun getTripMembers(@Path("tripId") tripId: String): BaseResponse<List<TripMember>>

    @POST("trips/{tripId}/addMember")
    suspend fun addTripMember(
        @Path("tripId") tripId: String,
        @Body userId: String
    ): BaseResponse<Boolean>
}