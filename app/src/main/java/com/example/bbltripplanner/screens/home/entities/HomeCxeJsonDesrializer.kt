package com.example.bbltripplanner.screens.home.entities

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class HomeCxeJsonDeserializer: JsonDeserializer<HomeCxeWidget> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): HomeCxeWidget {
        val jsonObj = json.asJsonObject
        val templateName = jsonObj.get("template_name")?.asString
            ?: jsonObj.get("templateName")?.asString
            ?: ""

        return when (templateName) {
            HomeCxeWidgetTypes.GREETING.templateName -> context.deserialize(json, HomeCxeWidget.GreetingWidget::class.java)
            HomeCxeWidgetTypes.IMAGE_CARROUSEL.templateName -> context.deserialize(json, HomeCxeWidget.ImageCarouselWidget::class.java)
            HomeCxeWidgetTypes.BUNDLE_ITEMS_WIDGET.templateName -> context.deserialize(json, HomeCxeWidget.BundleItemsWidget::class.java)
            HomeCxeWidgetTypes.NEWS_BANNER.templateName -> context.deserialize(json, HomeCxeWidget.NewsBannerWidget::class.java)
            HomeCxeWidgetTypes.LOCATION_FEED_CTA.templateName -> context.deserialize(json, HomeCxeWidget.TopPicksByLocationCtaWidget::class.java)
            else -> throw JsonParseException("Unknown template name: $templateName")
        }
    }
}