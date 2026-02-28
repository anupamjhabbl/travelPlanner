package com.example.bbltripplanner.screens.userTrip.usecases

import com.example.bbltripplanner.screens.userTrip.entity.Location
import com.example.bbltripplanner.screens.userTrip.repositories.LocationSearchRepository

class LocationSearchUseCase(
    private val locationSearchRepository: LocationSearchRepository
) {
    suspend fun getLocationSuggestions(key: String, query: String): List<Location> {
        return locationSearchRepository.getLocationSuggestions(key, query)
    }
}