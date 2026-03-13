package com.calorieai.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey val id: String,
    val mealType: String,
    val imageUri: String?,
    val createdAt: Long,
    val savedAt: Long?,
    val totalCalories: Double,
    val totalProteinG: Double,
    val totalCarbsG: Double,
    val totalFatG: Double,
    val overallConfidence: Float?,
    val analysisStatus: String,
    val userEdited: Boolean
)
