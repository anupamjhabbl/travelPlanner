package com.example.bbltripplanner.common.infra

import com.example.bbltripplanner.common.entity.NetworkConfiguration
import com.example.bbltripplanner.screens.home.entities.HomeCxeJsonDeserializer
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.core.component.KoinComponent

object InfraProvider :  KoinComponent {
    private val getBaseUrl by lazy {
//        "https://tripplanner-zgqj.onrender.com/api/"
        "http://10.0.2.2:3000/api/"
    }

    private val getLocationSearchBaseUrl by lazy {
        "https://api.locationiq.com/v1/"
    }

    val getNetworkConfiguration by lazy {
        NetworkConfiguration(getBaseUrl)
    }

    val getLocationSearchNetworkConfiguration by lazy {
        NetworkConfiguration(getLocationSearchBaseUrl)
    }

    val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(HomeCxeWidget::class.java, HomeCxeJsonDeserializer())
            .create()
    }
}