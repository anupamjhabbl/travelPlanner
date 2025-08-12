package com.example.bbltripplanner.screens.home.repositories

import com.example.bbltripplanner.screens.home.entities.HomeCxeResponse

interface HomeCxeLayoutRepository {
    suspend fun getHomeCxeLayout(): HomeCxeResponse
}