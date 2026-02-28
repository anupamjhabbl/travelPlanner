package com.example.bbltripplanner.screens.userTrip.repositoryImpl

import com.example.bbltripplanner.screens.userTrip.clients.LocationSearchClient
import com.example.bbltripplanner.screens.userTrip.entity.Location
import com.example.bbltripplanner.screens.userTrip.repositories.LocationSearchRepository

class LocationSearchNetwork(
    private val locationSearchClient: LocationSearchClient
): LocationSearchRepository {
    override suspend fun getLocationSuggestions(key: String, query: String): List<Location> {
        return locationSearchClient.getSuggestions(key, query)
    }
}