package com.example.bbltripplanner.screens.userTrip.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.AddActivityRequest
import com.example.bbltripplanner.screens.userTrip.entity.AddSpotRequest
import com.example.bbltripplanner.screens.userTrip.entity.Itinerary
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryActivity
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryActivityResponse
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryPlace
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryPlaceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ItineraryClient {
    @POST("trips/{tripId}/generateItinerary")
    suspend fun generateItinerary(@Path("tripId") tripId: String): Response<BaseResponse<Itinerary>>

    @GET("trips/{tripId}/getItinerary")
    suspend fun getItinerary(@Path("tripId") tripId: String): Response<BaseResponse<Itinerary>>

    @POST("itineraries/{itineraryId}/spots")
    suspend fun addSpot(
        @Path("itineraryId") itineraryId: String,
        @Body request: AddSpotRequest
    ): Response<BaseResponse<ItineraryPlaceResponse>>

    @GET("itineraries/{itineraryId}/spots")
    suspend fun getSpots(@Path("itineraryId") itineraryId: String): Response<BaseResponse<List<ItineraryPlace>>>

    @POST("spots/{spotId}/activities")
    suspend fun addActivity(
        @Path("spotId") spotId: String,
        @Body request: AddActivityRequest
    ): Response<BaseResponse<ItineraryActivity>>

    @GET("spots/{spotId}/activities")
    suspend fun getActivities(@Path("spotId") spotId: String): Response<BaseResponse<ItineraryActivityResponse>>

    @PUT("activities/{activityId}")
    suspend fun updateActivity(
        @Path("activityId") activityId: String,
        @Body request: AddActivityRequest
    ): Response<BaseResponse<ItineraryActivity>>

    @DELETE("activities/{activityId}")
    suspend fun deleteActivity(@Path("activityId") activityId: String): Response<BaseResponse<Unit>>
}
