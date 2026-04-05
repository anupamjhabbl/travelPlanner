package com.example.bbltripplanner.screens.userTrip.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.Itinerary
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryPlaceDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ItineraryClient {
    @GET("getItinerary/{tripId}")
    suspend fun getItinerary(@Path("tripId") tripId: String): Response<BaseResponse<Itinerary>>

    @GET("getItineraryPlaceDetail/{placeId}")
    suspend fun getItineraryPlaceDetail(@Path("placeId") placeId: String): Response<BaseResponse<ItineraryPlaceDetail>>
}
