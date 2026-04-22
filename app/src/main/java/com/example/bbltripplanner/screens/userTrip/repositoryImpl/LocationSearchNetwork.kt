package com.example.bbltripplanner.screens.userTrip.repositoryImpl

import com.example.bbltripplanner.screens.userTrip.clients.LocationSearchClient
import com.example.bbltripplanner.screens.userTrip.entity.Location
import com.example.bbltripplanner.screens.userTrip.entity.LocationSuggestionModel
import com.example.bbltripplanner.screens.userTrip.repositories.LocationSearchRepository

class LocationSearchNetwork(
    private val locationSearchClient: LocationSearchClient
): LocationSearchRepository {
    override suspend fun getLocationSuggestions(key: String, query: String): List<Location> {
        return locationSearchClient.getSuggestions(key, query).toLocationModel()
    }

    private fun List<LocationSuggestionModel>.toLocationModel(): List<Location> {
        return map {
            Location(
                lat = it.lat,
                lon = it.lon,
                displayName = it.displayName,
                address = it.address
            )
        }
    }
}