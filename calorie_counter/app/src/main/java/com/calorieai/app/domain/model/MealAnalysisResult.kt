package com.calorieai.app.domain.model

/**
 * Normalized result of AI meal analysis.
 * Never pass raw backend DTOs to UI; use this domain model after validation.
 */
data class MealAnalysisResult(
    val analysisId: String,
    val mealType: MealType,
    val captureTimestamp: Long,
    val imageUri: String?,
    val analysisStatus: AnalysisStatus,
    val foods: List<FoodItem>,
    val totals: NutritionTotals,
    val overallConfidence: Float?,
    val userEdited: Boolean = false
)
