package com.calorieai.app.domain.usecase

import com.calorieai.app.data.repository.MealRepository
import com.calorieai.app.domain.model.Meal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMealByIdUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    /** Observes a single meal by id. Emits updated meal when data changes (e.g. after edit). */
    fun invoke(mealId: String): Flow<Meal?> = mealRepository.observeMealById(mealId)
}
