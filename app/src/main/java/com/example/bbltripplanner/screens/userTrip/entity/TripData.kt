package com.example.bbltripplanner.screens.userTrip.entity

import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.screens.home.entities.Location
import com.google.gson.annotations.SerializedName

data class TripData(
    @SerializedName(value = "tripId", alternate = ["trip_id"])
    val tripId: String? = null,
    @SerializedName(value = "tripName", alternate = ["trip_name"])
    val tripName: String  = "",
    @SerializedName(value = "tripLocation", alternate = ["trip_location"])
    val tripLocation: Location? = null,
    @SerializedName(value = "startDate", alternate = ["start_date"])
    val startDate: String? = null,
    @SerializedName(value = "endDate", alternate = ["end_date"])
    val endDate: String? = null,
    @SerializedName(value = "tripMates", alternate = ["trip_mates"])
    val tripMates: List<User> = emptyList(),
    val visibility: TripVisibility = TripVisibility.PRIVATE,
    val status: TripStatus = TripStatus.PLANNED
)

enum class TripVisibility(val value: String) {
    PRIVATE("Private"), TRIP_MATES("TripMates"), PUBLIC("Public")
}

enum class TripStatus(val value: String) {
    ONGOING("OnGoing"), PLANNED("Planned"), COMPLETED("Completed")
}
