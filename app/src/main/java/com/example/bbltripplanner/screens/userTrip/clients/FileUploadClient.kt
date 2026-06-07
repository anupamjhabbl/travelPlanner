package com.example.bbltripplanner.screens.userTrip.clients

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Url

interface FileUploadClient {
    @PUT
    suspend fun uploadFile(
        @Url url: String,
        @Body file: RequestBody,
        @Header("Content-Type") contentType: String
    ): Response<Unit>
}
