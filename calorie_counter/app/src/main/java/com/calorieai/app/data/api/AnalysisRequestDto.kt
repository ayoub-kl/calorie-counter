package com.calorieai.app.data.api

/**
 * Request DTO for AI meal analysis API.
 * Map from domain/capture context when building the request.
 */
data class AnalysisRequestDto(
    val mealType: String? = null,
    val imageReference: String? = null,
    val clientTimestamp: Long? = null
)
