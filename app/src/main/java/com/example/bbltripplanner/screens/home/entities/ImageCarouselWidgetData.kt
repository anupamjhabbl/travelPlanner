package com.example.bbltripplanner.screens.home.entities

import com.google.gson.annotations.SerializedName

data class ImageCarouselWidgetData(
    @SerializedName(value = "carousel_items", alternate = ["carouselItems"])
    val carouselItems: List<CarouselItem>? = emptyList()
)

data class CarouselItem(
    val src: CarouselItemSrc? = null,
    val type: String? = null,
    val title: WidgetTitle?,
    @SerializedName("sub_title",alternate = ["subTitle"])
    val subTitle: WidgetTitle?,
    val id: String? = null,
)

data class CarouselItemSrc(
    val action: String? = null,
    val uri: String? = null,
    val ext: String? = null
)
