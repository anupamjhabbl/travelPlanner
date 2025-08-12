package com.example.bbltripplanner.screens.home.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BundleItemsWidgetData(
    val header: BundleItemWidgetHeader?,
    @SerializedName(value = "background_color", alternate = ["backgroundColor"])
    val backgroundColor: List<String>?,
    val content: BundleContent?,
    @SerializedName(value = "action_header", alternate = ["actionHeader"])
    val actionHeader: HomeBundleWidgetActionHeader?
)

data class BundleItemWidgetHeader(
    val text : String,
    val style : WidgetTextStyle?,
    val icon: WidgetImage?
) : Serializable

data class WidgetImage(
    val ext: String,
    val uri: String
)

data class WidgetTextStyle (
    @SerializedName("text_style", alternate = ["textStyle"])
    val textStyle : String?,
    @SerializedName("text_color", alternate = ["textColor"])
    val textColor : String
) : Serializable

data class BundleContent(
    @SerializedName(value = "bundle_data", alternate = ["bundleData"])
    val bundleData: String,
    val deeplink: String? = null
)

data class HomeBundleWidgetActionHeader(
    val text: String? = null,
    val style: HomeBundleWidgetActionHeaderStyle? = null
)

data class HomeBundleWidgetActionHeaderStyle(
    @SerializedName(value = "text_style", alternate = ["textStyle"])
    val textStyle: HomeWidgetActionHeaderTextStyle = HomeWidgetActionHeaderTextStyle.NORMAL,
    @SerializedName(value = "text_color", alternate = ["textColor"])
    val textColor: String = "#FFFFFF"
)

enum class HomeWidgetActionHeaderTextStyle(val value: String) {
    NORMAL("normal"),
    UNDERLINE("underline")
}
