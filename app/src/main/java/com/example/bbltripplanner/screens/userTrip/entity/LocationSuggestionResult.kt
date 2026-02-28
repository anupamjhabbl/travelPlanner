package com.example.bbltripplanner.screens.userTrip.entity

import com.google.gson.annotations.SerializedName

data class Location(
    val lat: String?,
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