package com.example.bbltripplanner.screens.userTrip.entity

import com.google.gson.annotations.SerializedName

data class LocationRequestModel(
    @SerializedName("name")
    val name: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("latitude")
    val latitude: String?,
    @SerializedName("longitude")
    val longitude: String?
)

fun Location.toModel(): LocationRequestModel {
    return LocationRequestModel(
        name = address?.name,
        address = displayName,
        city = address?.city,
        state = address?.state,
        country = address?.country,
        latitude = lat,
        longitude = lon
    )
}
