package com.calorieai.app.domain.usecase

import com.calorieai.app.domain.model.MealAnalysisResult
import com.calorieai.app.domain.model.MealType
import com.calorieai.app.data.repository.AnalysisRepository
import javax.inject.Inject

class AnalyzeMealUseCase @Inject constructor(
    private val analysisRepository: AnalysisRepository
) {
    suspend operator fun invoke(mealType: MealType, imageUri: String): Result<MealAnalysisResult> {
        return analysisRepository.analyzeMeal(mealType, imageUri)
    }
}
