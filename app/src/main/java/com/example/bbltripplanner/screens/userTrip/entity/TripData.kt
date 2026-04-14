package com.example.bbltripplanner.screens.userTrip.entity

import androidx.compose.runtime.Stable
import com.example.bbltripplanner.common.entity.User
import com.google.gson.annotations.SerializedName

@Stable
data class TripData(
    @SerializedName(value = "tripId", alternate = ["trip_id"])
    val tripId: String? = null,
    @SerializedName(value = "tripName", alternate = ["trip_name"])
    val tripName: String  = "",
    @SerializedName(value = "tripLocation", alternate = ["trip_location"])
    val whereTo: Location? = null,
    @SerializedName(value = "startDate", alternate = ["start_date"])
    val startDate: Long? = null,
    @SerializedName(value = "endDate", alternate = ["end_date"])
    val endDate: Long? = null,
    @SerializedName(value = "invitedMembers", alternate = ["invited_members"])
    val invitedMembers: List<User> = emptyList(),
    @SerializedName(value = "tripVisibility", alternate = ["trip_visibility"])
    val visibility: TripVisibility = TripVisibility.PRIVATE,
    @SerializedName(value = "acceptanceNeeded", alternate = ["acceptance_needed"])
    val acceptanceNeeded: Boolean = false
)

data class TripDataRequestModel(
    @SerializedName(value = "tripId", alternate = ["trip_id"])
    val tripId: String? = null,
    @SerializedName(value = "tripName", alternate = ["trip_name"])
    val tripName: String  = "",
    @SerializedName(value = "tripLocation", alternate = ["trip_location"])
    val whereTo: Location? = null,
    @SerializedName(value = "startDate", alternate = ["start_date"])
    val startDate: Long? = null,
    @SerializedName(value = "endDate", alternate = ["end_date"])
    val endDate: Long? = null,
    @SerializedName(value = "invitedMembers", alternate = ["invited_members"])
    val invitedMembers: List<String> = emptyList(),
    @SerializedName(value = "tripVisibility", alternate = ["trip_visibility"])
    val visibility: TripVisibility = TripVisibility.PRIVATE
)

enum class TripVisibility(val value: String) {
    PRIVATE("Private"), PUBLIC("Public");

    companion object {
        fun getEnum(value: String): TripVisibility {
            return TripVisibility.entries.find { visibility ->
                visibility.value == value
            } ?: PRIVATE
        }
    }
}
