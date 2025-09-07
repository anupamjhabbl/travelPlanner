package com.example.bbltripplanner.common.entity

class TripPlannerException (
    val errorCode: Int,
    message: String
) : Exception(message)