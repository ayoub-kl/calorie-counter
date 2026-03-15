package com.calorieai.app.data.repository

import com.calorieai.app.data.local.MealDao
import com.calorieai.app.data.local.MealFoodItemEntity
import com.calorieai.app.data.local.MealEntity
import com.calorieai.app.data.local.MealWithFoodItems
import com.calorieai.app.domain.model.AnalysisStatus
import com.calorieai.app.domain.model.FoodItem
import com.calorieai.app.domain.model.Meal
import com.calorieai.app.domain.model.MealType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [DefaultMealRepository].
 * Mocks [MealDao]; verifies persistence and mapping.
 */
class DefaultMealRepositoryTest {

    private val mealDao = mockk<MealDao>(relaxed = true)
    private val repository = DefaultMealRepository(mealDao)

    @Test
    fun saveMeal_callsDaoWithMappedEntity() = runTest {
        val meal = Meal(
            id = "meal-1",
            mealType = MealType.LUNCH,
            imageUri = "content://img",
            createdAt = 1000L,
            savedAt = 2000L,
            totalCalories = 450.0,
            totalProteinG = 30.0,
            totalCarbsG = 40.0,
            totalFatG = 15.0,
            overallConfidence = 0.85f,
            analysisStatus = AnalysisStatus.COMPLETED,
            userEdited = true,
            foodItems = listOf(
                FoodItem("f1", "Rice", 1.0, "cup", 200.0, 4.0, 44.0, 0.4, null),
                FoodItem("f2", "Chicken", 100.0, "g", 250.0, 35.0, 0.0, 10.0, null)
            )
        )
        repository.saveMeal(meal)
        coVerify {
            mealDao.insertMealWithFoods(
                match { it.id == "meal-1" && it.mealType == "LUNCH" && it.totalCalories == 450.0 },
                match { it.size == 2 && it.all { e -> e.mealId == "meal-1" } }
            )
        }
    }

    @Test
    fun observeAllMeals_mapsEntitiesToDomainMeals() = runTest {
        val entity = MealEntity(
            id = "m1",
            mealType = "BREAKFAST",
            imageUri = null,
            createdAt = 500L,
            savedAt = 600L,
            totalCalories = 300.0,
            totalProteinG = 12.0,
            totalCarbsG = 35.0,
            totalFatG = 8.0,
            overallConfidence = 0.9f,
            analysisStatus = "COMPLETED",
            userEdited = false
        )
        val foodEntity = MealFoodItemEntity(
            id = "food1",
            mealId = "m1",
            name = "Oatmeal",
            quantity = 1.0,
            unit = "bowl",
            calories = 300.0,
            proteinG = 12.0,
            carbsG = 35.0,
            fatG = 8.0,
            confidence = 0.9f
        )
        val withFoods = MealWithFoodItems(entity, listOf(foodEntity))
        coEvery { mealDao.observeAllWithFoods() } returns flowOf(listOf(withFoods))

        val result = repository.observeAllMeals().first()
        assertEquals(1, result.size)
        val meal = result[0]
        assertEquals("m1", meal.id)
        assertEquals(MealType.BREAKFAST, meal.mealType)
        assertEquals(300.0, meal.totalCalories, 0.0)
        assertEquals(1, meal.foodItems.size)
        assertEquals("Oatmeal", meal.foodItems[0].name)
        assertEquals("bowl", meal.foodItems[0].unit)
    }

    @Test
    fun getMealById_returnsMappedMealWhenExists() = runTest {
        val entity = MealEntity(
            id = "m2",
            mealType = "DINNER",
            imageUri = "content://x",
            createdAt = 100L,
            savedAt = 200L,
            totalCalories = 600.0,
            totalProteinG = 40.0,
            totalCarbsG = 50.0,
            totalFatG = 20.0,
            overallConfidence = null,
            analysisStatus = "COMPLETED",
            userEdited = true
        )
        val withFoods = MealWithFoodItems(entity, emptyList())
        coEvery { mealDao.getMealWithFoodsById("m2") } returns withFoods

        val meal = repository.getMealById("m2")
        assertNotNull(meal)
        assertEquals("m2", meal!!.id)
        assertEquals(MealType.DINNER, meal.mealType)
        assertEquals(600.0, meal.totalCalories, 0.0)
        assertTrue(meal.userEdited)
    }

    @Test
    fun getMealById_returnsNullWhenNotFound() = runTest {
        coEvery { mealDao.getMealWithFoodsById("missing") } returns null
        val meal = repository.getMealById("missing")
        assertEquals(null, meal)
    }
}
