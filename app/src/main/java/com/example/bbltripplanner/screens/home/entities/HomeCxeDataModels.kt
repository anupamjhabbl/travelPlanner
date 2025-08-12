package com.example.bbltripplanner.screens.home.entities

import com.google.gson.annotations.SerializedName

data class HomeCxeResponse(
    val sections: List<HomeCxeLayoutSection> = emptyList(),
)

data class HomeCxeLayoutSection(
    val name: String = "",
    val widgets: List<HomeCxeWidget> = emptyList(),
)

data class WidgetMetaData(
    val style: WidgetStyle,
    @SerializedName(value = "auto_scroll", alternate = ["autoScroll"])
    val autoScroll: AutoScroll?,
)

data class AutoScroll(
    val enabled: Boolean,
    val time: Int
)

data class WidgetStyle(
    @SerializedName(value = "aspect_ratio", alternate = ["aspectRatio"])
    val aspectRatio: String,
    @SerializedName(value = "right_icon", alternate = ["rightIcon"])
    val rightIcon: Boolean
)

sealed class HomeCxeWidget(
    val name: String = "",
    @SerializedName(value = "template_name", alternate = ["templateName"])
    val templateName: String = "",
    val metadata: WidgetMetaData? =  null
) {
    data class GreetingWidget(
        val  data: GreetingWidgetData
    ): HomeCxeWidget("")

    data class ImageCarouselWidget(
        val data: ImageCarouselWidgetData
    ): HomeCxeWidget("")

    data class BundleItemsWidget(
        val data: BundleItemsWidgetData
    ): HomeCxeWidget("")

    data class NewsBannerWidget(
        val data: NewsBannerWidgetData
    ): HomeCxeWidget("")

    data class TopPicksByLocationCtaWidget(
        val data: TopPicksByLocationCtaWidgetData
    ): HomeCxeWidget("")
}
