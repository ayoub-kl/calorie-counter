package com.calorieai.app.domain.usecase

import com.calorieai.app.domain.model.Meal
import com.calorieai.app.data.repository.MealRepository
import javax.inject.Inject

class SaveMealUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(meal: Meal) {
        mealRepository.saveMeal(meal)
    }
}
