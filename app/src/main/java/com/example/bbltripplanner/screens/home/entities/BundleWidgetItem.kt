package com.example.bbltripplanner.screens.home.entities

import com.google.gson.annotations.SerializedName

data class BundleWidgetItem(
    @SerializedName("item_id", alternate = ["itemId"])
    val itemId: String,
    @SerializedName(value = "trip_image", alternate = ["tripImage"])
    val tripImage: List<String> = emptyList(),
    @SerializedName(value = "trip_name", alternate = ["tripName"])
    val tripName: String = "",
    @SerializedName(value = "trip_expenses", alternate = ["tripExpenses"])
    val tripExpenses: List<Long>?
)


