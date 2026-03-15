package com.calorieai.app.data.local

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Room relation model: one meal with its food items.
 */
data class MealWithFoodItems(
    @Embedded val meal: MealEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "mealId",
        entity = MealFoodItemEntity::class
    )
    val foodItems: List<MealFoodItemEntity>
)
