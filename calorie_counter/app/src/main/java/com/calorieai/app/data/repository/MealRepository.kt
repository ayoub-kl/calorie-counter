package com.calorieai.app.data.repository

import com.calorieai.app.domain.model.Meal
import kotlinx.coroutines.flow.Flow

/**
 * Repository boundary for meal persistence and retrieval.
 * Hides DTO/entity details from domain and UI.
 */
interface MealRepository {
    fun observeAllMeals(): Flow<List<Meal>>
    fun observeMealById(id: String): Flow<Meal?>
    suspend fun getMealById(id: String): Meal?
    suspend fun saveMeal(meal: Meal)
}
