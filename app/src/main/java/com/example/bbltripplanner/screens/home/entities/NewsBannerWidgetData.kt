package com.example.bbltripplanner.screens.home.entities

import com.google.gson.annotations.SerializedName

data class NewsBannerWidgetData(
    val header: WidgetTitle?,
    @SerializedName("action_header",alternate = ["actionHeader"])
    val actionHeader: HomeBundleWidgetActionHeader?,
    @SerializedName(value = "background_color", alternate = ["backgroundColor"])
    val backgroundColor: List<String>?
)

data class WidgetTitle(
    val title: String = "",
    @SerializedName(value = "sub_title", alternate = ["subTitle"])
    val subTitle: String = ""
)
