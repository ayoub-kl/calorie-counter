package com.calorieai.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: MealEntity)

    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getById(id: String): MealEntity?

    @Query("SELECT * FROM meals ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<MealEntity>>

    @Transaction
    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getMealWithFoodsById(id: String): MealWithFoodItems?

    @Transaction
    @Query("SELECT * FROM meals WHERE id = :id")
    fun observeMealWithFoodsById(id: String): Flow<MealWithFoodItems?>

    @Transaction
    @Query("SELECT * FROM meals ORDER BY createdAt DESC")
    fun observeAllWithFoods(): Flow<List<MealWithFoodItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItems(items: List<MealFoodItemEntity>)

    @Transaction
    suspend fun insertMealWithFoods(meal: MealEntity, foodItems: List<MealFoodItemEntity>) {
        insert(meal)
        insertFoodItems(foodItems)
    }
}
