package com.calorieai.app.domain.usecase

import com.calorieai.app.domain.model.Meal
import com.calorieai.app.data.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMealsUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    fun invoke(): Flow<List<Meal>> = mealRepository.observeAllMeals()
}
