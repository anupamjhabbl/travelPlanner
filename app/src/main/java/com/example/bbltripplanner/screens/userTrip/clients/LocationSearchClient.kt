package com.example.bbltripplanner.screens.userTrip.clients

import com.example.bbltripplanner.screens.userTrip.entity.Location
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationSearchClient {
    @GET("autocomplete")
    suspend fun getSuggestions(@Query("key") key: String, @Query("q") query: String): List<Location>
}