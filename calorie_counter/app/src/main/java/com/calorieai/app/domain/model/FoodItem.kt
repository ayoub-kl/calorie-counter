package com.calorieai.app.domain.model

/**
 * Domain model for a single food item within a meal.
 * Supports estimated (AI) and corrected values; confidence for AI estimates.
 */
data class FoodItem(
    val id: String,
    val name: String,
    val quantity: Double,
    val unit: String,
    val calories: Double,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double,
    val confidence: Float?
)
