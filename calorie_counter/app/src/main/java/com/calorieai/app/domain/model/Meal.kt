package com.calorieai.app.domain.model

/**
 * Domain model for a saved or in-progress meal.
 * Supports AI analysis result and user corrections (userEdited).
 */
data class Meal(
    val id: String,
    val mealType: MealType,
    val imageUri: String?,
    val createdAt: Long,
    val savedAt: Long?,
    val totalCalories: Double,
    val totalProteinG: Double,
    val totalCarbsG: Double,
    val totalFatG: Double,
    val overallConfidence: Float?,
    val analysisStatus: AnalysisStatus,
    val userEdited: Boolean,
    val foodItems: List<FoodItem> = emptyList()
)

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
}

enum class AnalysisStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}
