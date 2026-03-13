package com.calorieai.app.data.repository

import com.calorieai.app.domain.model.MealAnalysisResult
import com.calorieai.app.domain.model.MealType

/**
 * Repository boundary for AI meal analysis.
 * Coordinates upload and maps validated response to domain [MealAnalysisResult].
 */
interface AnalysisRepository {
    suspend fun analyzeMeal(
        mealType: MealType,
        imageUri: String
    ): Result<MealAnalysisResult>
}
