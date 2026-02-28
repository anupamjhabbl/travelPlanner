package com.example.bbltripplanner.screens.home.entities

import com.example.bbltripplanner.screens.userTrip.entity.Location
import com.google.gson.annotations.SerializedName

data class TravelThreadsWidgetItem(
    @SerializedName(value = "thread_id", alternate = ["threadId"])
    val threadId: String,
    @SerializedName(value = "trip_name", alternate = ["tripName"])
    val tripName: String = "",
    @SerializedName(value = "trip_images", alternate = ["tripImage"])
    val tripImages: List<String> = emptyList(),
    @SerializedName(value = "trip_location", alternate = ["tripLocation"])
    val tripLocation: Location? = null,
    @SerializedName(value = "experience_description", alternate = ["experienceDescription"])
    val experienceDescription: String = "",
    @SerializedName(value = "trip_profile", alternate = ["tripProfile"])
    val tripProfile: TripProfile? = null,
    @SerializedName(value = "post_engagement", alternate = ["postEngagement"])
    val postEngagement: PostEngagement
)

data class PostEngagement(
    val likes: Int = 0,
    val comments: Int = 0,
    val views: Int = 0
)
