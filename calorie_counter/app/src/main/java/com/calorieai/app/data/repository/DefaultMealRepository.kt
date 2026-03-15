package com.calorieai.app.data.repository

import com.calorieai.app.data.local.MealDao
import com.calorieai.app.data.local.MealFoodItemDao
import com.calorieai.app.data.local.MealFoodItemEntity
import com.calorieai.app.data.local.MealEntity
import com.calorieai.app.data.local.MealWithFoodItems
import com.calorieai.app.domain.model.AnalysisStatus
import com.calorieai.app.domain.model.FoodItem
import com.calorieai.app.domain.model.Meal
import com.calorieai.app.domain.model.MealType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultMealRepository @Inject constructor(
    private val mealDao: MealDao
) : MealRepository {

    override fun observeAllMeals(): Flow<List<Meal>> =
        mealDao.observeAllWithFoods().map { list -> list.map { toMeal(it) } }

    override suspend fun getMealById(id: String): Meal? =
        mealDao.getMealWithFoodsById(id)?.let { toMeal(it) }

    override suspend fun saveMeal(meal: Meal) {
        mealDao.insertMealWithFoods(
            toMealEntity(meal),
            meal.foodItems.map { toFoodItemEntity(meal.id, it) }
        )
    }

    private fun toMeal(w: MealWithFoodItems): Meal = Meal(
        id = w.meal.id,
        mealType = MealType.valueOf(w.meal.mealType),
        imageUri = w.meal.imageUri,
        createdAt = w.meal.createdAt,
        savedAt = w.meal.savedAt,
        totalCalories = w.meal.totalCalories,
        totalProteinG = w.meal.totalProteinG,
        totalCarbsG = w.meal.totalCarbsG,
        totalFatG = w.meal.totalFatG,
        overallConfidence = w.meal.overallConfidence,
        analysisStatus = AnalysisStatus.valueOf(w.meal.analysisStatus),
        userEdited = w.meal.userEdited,
        foodItems = w.foodItems.map { toFoodItem(it) }
    )

    private fun toFoodItem(e: MealFoodItemEntity): FoodItem = FoodItem(
        id = e.id,
        name = e.name,
        quantity = e.quantity,
        unit = e.unit,
        calories = e.calories,
        proteinG = e.proteinG,
        carbsG = e.carbsG,
        fatG = e.fatG,
        confidence = e.confidence
    )

    private fun toMealEntity(m: Meal): MealEntity = MealEntity(
        id = m.id,
        mealType = m.mealType.name,
        imageUri = m.imageUri,
        createdAt = m.createdAt,
        savedAt = m.savedAt,
        totalCalories = m.totalCalories,
        totalProteinG = m.totalProteinG,
        totalCarbsG = m.totalCarbsG,
        totalFatG = m.totalFatG,
        overallConfidence = m.overallConfidence,
        analysisStatus = m.analysisStatus.name,
        userEdited = m.userEdited
    )

    private fun toFoodItemEntity(mealId: String, f: FoodItem): MealFoodItemEntity = MealFoodItemEntity(
        id = f.id,
        mealId = mealId,
        name = f.name,
        quantity = f.quantity,
        unit = f.unit,
        calories = f.calories,
        proteinG = f.proteinG,
        carbsG = f.carbsG,
        fatG = f.fatG,
        confidence = f.confidence
    )
}
