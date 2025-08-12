package com.example.bbltripplanner.common.infra

import com.example.bbltripplanner.common.entity.NetworkConfiguration
import com.example.bbltripplanner.screens.home.entities.HomeCxeJsonDeserializer
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.core.component.KoinComponent

object InfraProvider :  KoinComponent {
    private val getBaseUrl by lazy {
        "http://10.0.2.2:3000/tripPlanner/"
    }

    val getNetworkConfiguration by lazy {
        NetworkConfiguration(getBaseUrl)
    }

    val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(HomeCxeWidget::class.java, HomeCxeJsonDeserializer())
            .create()
    }
}