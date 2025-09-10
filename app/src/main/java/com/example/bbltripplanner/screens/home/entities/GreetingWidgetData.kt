package com.example.bbltripplanner.screens.home.entities

import com.google.gson.annotations.SerializedName

data class GreetingWidgetData(
    @SerializedName(value = "greeting_text", alternate = ["greetingText"])
    val greetingText: String = "",
    @SerializedName(value = "greeting_text_style", alternate = ["greetingTextStyle"])
    val greetingTextStyle: GreetingTextStyle?
)

data class GreetingTextStyle(
    @SerializedName(value = "text_style", alternate = ["textStyle"])
    val textStyle: HomeWidgetActionHeaderTextStyle = HomeWidgetActionHeaderTextStyle.NORMAL,
    @SerializedName(value = "text_color", alternate = ["textColor"])
    val textColor: String = "#FFFFFF",
    @SerializedName(value = "font_size", alternate = ["fontSize"])
    val fontSize: Int? = null
)
