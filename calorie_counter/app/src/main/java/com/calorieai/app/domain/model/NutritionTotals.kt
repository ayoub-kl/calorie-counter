package com.calorieai.app.domain.model

/**
 * Aggregated nutrition totals (calories and macros).
 * Used for meal-level and daily totals.
 */
data class NutritionTotals(
    val calories: Double,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double
)
