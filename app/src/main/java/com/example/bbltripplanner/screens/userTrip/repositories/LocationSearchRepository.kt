package com.example.bbltripplanner.screens.userTrip.repositories

import com.example.bbltripplanner.screens.userTrip.entity.Location

interface LocationSearchRepository {
    suspend fun getLocationSuggestions(key: String, query: String): List<Location>
}