package com.calorieai.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealFoodItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MealFoodItemEntity>)

    @Query("SELECT * FROM meal_food_items WHERE mealId = :mealId")
    suspend fun getByMealId(mealId: String): List<MealFoodItemEntity>
}
