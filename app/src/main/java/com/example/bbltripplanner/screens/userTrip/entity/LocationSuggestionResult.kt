package com.example.bbltripplanner.screens.userTrip.entity

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("latitude")
    val lat: String?,
    @SerializedName("longitude")
    val lon: String?,
    @SerializedName("display_name")
    val displayName: String?,
    val address: Address?
)

data class Address(
    val name: String?,
    val road: String?,
    val city: String?,
    val state: String?,
    val postcode: String?,
    val country: String?
)