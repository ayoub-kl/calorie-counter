package com.calorieai.app.data.api

import com.calorieai.app.domain.model.AnalysisStatus
import com.calorieai.app.domain.model.FoodItem
import com.calorieai.app.domain.model.MealAnalysisResult
import com.calorieai.app.domain.model.MealType
import com.calorieai.app.domain.model.NutritionTotals
import java.util.UUID

/**
 * Maps and validates AI response DTO to domain [MealAnalysisResult].
 * Handles null/missing fields, normalizes numeric values, never exposes DTO to UI.
 */
object AnalysisMapper {

    private const val MIN_CONFIDENCE = 0f
    private const val MAX_CONFIDENCE = 1f
    private const val MIN_NON_NEGATIVE = 0.0

    /**
     * Validates and maps [AnalysisResponseDto] to [MealAnalysisResult].
     * Returns [Result.failure] if response is invalid or empty; otherwise [Result.success].
     */
    fun toDomain(
        dto: AnalysisResponseDto,
        mealType: MealType,
        captureTimestamp: Long,
        imageUri: String?
    ): Result<MealAnalysisResult> = runCatching {
        val analysisId = dto.analysisId?.takeIf { it.isNotBlank() } ?: UUID.randomUUID().toString()
        val overallConfidence = normalizeConfidence(dto.overallConfidence)
        val foodItems = (dto.foods.orEmpty())
            .mapNotNull { mapFoodItem(it) }
            .ifEmpty { null }?.let { it } ?: emptyList()

        val totalsDto = dto.totals
        val totals = NutritionTotals(
            calories = normalizeNonNegative(totalsDto?.calories) ?: foodItems.sumOf { it.calories },
            proteinG = normalizeNonNegative(totalsDto?.protein_g) ?: foodItems.sumOf { it.proteinG },
            carbsG = normalizeNonNegative(totalsDto?.carbs_g) ?: foodItems.sumOf { it.carbsG },
            fatG = normalizeNonNegative(totalsDto?.fat_g) ?: foodItems.sumOf { it.fatG }
        )

        MealAnalysisResult(
            analysisId = analysisId,
            mealType = mealType,
            captureTimestamp = captureTimestamp,
            imageUri = imageUri,
            analysisStatus = AnalysisStatus.COMPLETED,
            foods = foodItems,
            totals = totals,
            overallConfidence = overallConfidence,
            userEdited = false
        )
    }

    private fun mapFoodItem(dto: FoodItemDto): FoodItem? {
        val name = dto.name?.trim()?.takeIf { it.isNotBlank() } ?: return null
        return FoodItem(
            id = UUID.randomUUID().toString(),
            name = name,
            quantity = normalizeNonNegative(dto.quantity) ?: MIN_NON_NEGATIVE,
            unit = dto.unit?.trim()?.takeIf { it.isNotBlank() } ?: "serving",
            calories = normalizeNonNegative(dto.calories) ?: MIN_NON_NEGATIVE,
            proteinG = normalizeNonNegative(dto.protein_g) ?: MIN_NON_NEGATIVE,
            carbsG = normalizeNonNegative(dto.carbs_g) ?: MIN_NON_NEGATIVE,
            fatG = normalizeNonNegative(dto.fat_g) ?: MIN_NON_NEGATIVE,
            confidence = normalizeConfidence(dto.confidence)
        )
    }

    private fun normalizeConfidence(value: Double?): Float? {
        if (value == null) return null
        val clamped = value.coerceIn(MIN_CONFIDENCE.toDouble(), MAX_CONFIDENCE.toDouble())
        return clamped.toFloat()
    }

    private fun normalizeNonNegative(value: Double?): Double? {
        if (value == null || value.isNaN()) return null
        return value.coerceAtLeast(MIN_NON_NEGATIVE)
    }
}
