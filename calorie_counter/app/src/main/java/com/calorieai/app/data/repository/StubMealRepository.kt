package com.calorieai.app.data.repository

import com.calorieai.app.domain.model.Meal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Stub implementation for skeleton. Replace with real Room-backed repository in later prompts.
 */
class StubMealRepository @Inject constructor() : MealRepository {

    override fun observeAllMeals(): Flow<List<Meal>> = flowOf(emptyList())

    override suspend fun getMealById(id: String): Meal? = null

    override suspend fun saveMeal(meal: Meal) {
        // No-op until Room and mappers are implemented
    }
}
