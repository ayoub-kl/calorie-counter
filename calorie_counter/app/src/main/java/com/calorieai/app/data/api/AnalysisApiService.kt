package com.calorieai.app.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Retrofit service for AI meal analysis.
 * Request: multipart form with image file and meal type.
 * Response: [AnalysisResponseDto] (validated and mapped to domain in repository).
 */
interface AnalysisApiService {

    @Multipart
    @POST("analyze")
    suspend fun analyzeMealPhoto(
        @Part image: MultipartBody.Part,
        @Part("meal_type") mealType: RequestBody
    ): AnalysisResponseDto
}
