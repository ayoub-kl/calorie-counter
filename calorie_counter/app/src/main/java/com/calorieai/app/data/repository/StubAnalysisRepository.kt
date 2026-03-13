package com.calorieai.app.data.repository

import com.calorieai.app.domain.model.MealAnalysisResult
import com.calorieai.app.domain.model.MealType
import javax.inject.Inject

/**
 * Stub implementation for skeleton. Replace with real API and validation in later prompts.
 */
class StubAnalysisRepository @Inject constructor() : AnalysisRepository {

    override suspend fun analyzeMeal(
        mealType: MealType,
        imageUri: String
    ): Result<MealAnalysisResult> {
        return Result.failure(UnsupportedOperationException("Analysis not implemented yet"))
    }
}
