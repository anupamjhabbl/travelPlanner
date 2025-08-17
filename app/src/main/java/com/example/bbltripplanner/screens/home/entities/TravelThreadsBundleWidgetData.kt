package com.example.bbltripplanner.screens.home.entities

import com.google.gson.annotations.SerializedName

data class TravelThreadsBundleWidgetData(
    val header: BundleItemWidgetHeader?,
    @SerializedName(value = "background_color", alternate = ["backgroundColor"])
    val backgroundColor: List<String>?,
    val content: BundleContent?,
    @SerializedName(value = "action_header", alternate = ["actionHeader"])
    val actionHeader: HomeBundleWidgetActionHeader?
)
