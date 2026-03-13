package com.calorieai.app.data.api

/**
 * Raw response DTO from AI meal analysis backend.
 * Must be validated and mapped to domain [MealAnalysisResult]; never expose directly to UI.
 */
data class AnalysisResponseDto(
    val analysisId: String? = null,
    val mealType: String? = null,
    val overallConfidence: Double? = null,
    val warnings: List<String>? = null,
    val foods: List<FoodItemDto>? = null,
    val totals: TotalsDto? = null
)

data class FoodItemDto(
    val name: String? = null,
    val quantity: Double? = null,
    val unit: String? = null,
    val calories: Double? = null,
    val protein_g: Double? = null,
    val carbs_g: Double? = null,
    val fat_g: Double? = null,
    val confidence: Double? = null
)

data class TotalsDto(
    val calories: Double? = null,
    val protein_g: Double? = null,
    val carbs_g: Double? = null,
    val fat_g: Double? = null
)
