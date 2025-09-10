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
    ): HomeCxeWidget("", HomeCxeWidgetTypes.GREETING.templateName)

    data class ImageCarouselWidget(
        val data: ImageCarouselWidgetData
    ): HomeCxeWidget("", HomeCxeWidgetTypes.IMAGE_CARROUSEL.templateName)

    data class BundleItemsWidget(
        val data: BundleItemsWidgetData
    ): HomeCxeWidget("", HomeCxeWidgetTypes.BUNDLE_ITEMS_WIDGET.templateName)

    data class NewsBannerWidget(
        val data: NewsBannerWidgetData
    ): HomeCxeWidget("", HomeCxeWidgetTypes.NEWS_BANNER.templateName)

    data class TopPicksByLocationCtaWidget(
        val data: TopPicksByLocationCtaWidgetData
    ): HomeCxeWidget("", HomeCxeWidgetTypes.LOCATION_FEED_CTA.templateName)

    data class TravelThreadsBundleWidget(
        val data: TravelThreadsBundleWidgetData
    ): HomeCxeWidget("", HomeCxeWidgetTypes.TRAVEL_THREADS_BUNDLE_WIDGET.templateName)

    data class UserTripBundleWidget(
        val data: UserTripBundleWidgetData
    ): HomeCxeWidget("", HomeCxeWidgetTypes.USER_TRIP_BUNDLE_WIDGET.templateName)

    fun getWidgetType(): HomeCxeWidgetTypes {
        return HomeCxeWidgetTypes.entries.find {
            it.templateName == templateName
        } ?: HomeCxeWidgetTypes.INVALID
    }
}
