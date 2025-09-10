package com.example.bbltripplanner.screens.home.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.home.entities.BundleWidgetItem
import com.example.bbltripplanner.screens.home.entities.HomeCxeResponse
import com.example.bbltripplanner.screens.home.entities.TravelThreadsWidgetItem
import com.example.bbltripplanner.screens.home.entities.UserTripWidgetItem
import retrofit2.http.GET
import retrofit2.http.Url

interface HomeCxeClient {
    @GET("homeCxeLayout")
    suspend fun getHomeCxeData(): BaseResponse<HomeCxeResponse>

    @GET
    suspend fun getTravelThreadBundleData(@Url url: String): BaseResponse<List<TravelThreadsWidgetItem>>

    @GET
    suspend fun getUserTripBundleData(@Url bundleUri: String): BaseResponse<List<UserTripWidgetItem>>

    @GET
    suspend fun getBundleWidgetData(@Url bundleUri: String): BaseResponse<List<BundleWidgetItem>>
}