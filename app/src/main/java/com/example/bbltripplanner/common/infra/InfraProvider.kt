package com.example.bbltripplanner.common.infra

import com.example.bbltripplanner.common.entity.NetworkConfiguration
import org.koin.core.component.KoinComponent

object InfraProvider :  KoinComponent {
    private val getBaseUrl by lazy {
        "https://run.mocky.io/v3/"
    }

    val getNetworkCConfiguration by lazy {   // TODO: may be this is initialized from internet
        NetworkConfiguration(getBaseUrl)
    }
}