package com.example.bbltripplanner.screens.userTrip.repositories

import androidx.room.Query
import com.example.bbltripplanner.screens.userTrip.entity.Location

interface LocationSearchRepository {
    suspend fun getLocationSuggestions(key: String, query: String): List<Location>
}