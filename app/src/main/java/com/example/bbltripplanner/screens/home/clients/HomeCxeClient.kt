package com.example.bbltripplanner.screens.home.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.home.entities.HomeCxeResponse
import retrofit2.http.GET

interface HomeCxeClient {
    @GET("homeCxeLayout")
    suspend fun getHomeCxeData(): BaseResponse<HomeCxeResponse>
}