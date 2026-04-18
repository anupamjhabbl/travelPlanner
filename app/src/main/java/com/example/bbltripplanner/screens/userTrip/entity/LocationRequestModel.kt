package com.example.bbltripplanner.screens.userTrip.entity

import com.google.gson.annotations.SerializedName

data class LocationRequestModel(
    @SerializedName("name")
    val name: String?,
    @SerializedName("display_name")
    val displayName: String?,
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
