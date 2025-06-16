package com.example.bbltripplanner.common.entity

class ApiFailureException(
    private val failureMessage: String?
): RuntimeException() {
    override val message: String?
        get() = failureMessage
}