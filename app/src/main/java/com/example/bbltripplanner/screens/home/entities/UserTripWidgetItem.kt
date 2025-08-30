package com.example.bbltripplanner.screens.home.entities

import com.google.gson.annotations.SerializedName

data class UserTripWidgetItem(
    @SerializedName("user_trip_id", alternate = ["userTripId"])
    val userTripId: String,
    @SerializedName(value = "trip_name", alternate = ["tripName"])
    val tripName: String = "",
    @SerializedName(value = "trip_images", alternate = ["tripImages"])
    val tripImages: List<String> = emptyList(),
    @SerializedName(value = "trip_profile", alternate = ["tripProfile"])
    val tripProfile: TripProfile? = null,
    @SerializedName(value = "trip_location", alternate = ["tripLocation"])
    val tripLocation: Location? = null,
    val date: String? = null
)

data class TripProfile(
    @SerializedName(value = "profile_name", alternate = ["profileName"])
    val profileName: String = "",
    @SerializedName(value = "profile_image", alternate = ["profileImage"])
    val profileImage: String = ""
)
