package com.calorieai.app.domain

import com.calorieai.app.domain.model.FoodItem
import com.calorieai.app.domain.model.NutritionTotals
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for nutrition totals computation.
 * Totals are computed as sums of food item calories and macros.
 */
class NutritionTotalsTest {

    private fun computeTotals(foods: List<FoodItem>): NutritionTotals = NutritionTotals(
        calories = foods.sumOf { it.calories },
        proteinG = foods.sumOf { it.proteinG },
        carbsG = foods.sumOf { it.carbsG },
        fatG = foods.sumOf { it.fatG }
    )

    @Test
    fun emptyList_returnsZeroTotals() {
        val totals = computeTotals(emptyList())
        assertEquals(0.0, totals.calories, 0.0)
        assertEquals(0.0, totals.proteinG, 0.0)
        assertEquals(0.0, totals.carbsG, 0.0)
        assertEquals(0.0, totals.fatG, 0.0)
    }

    @Test
    fun singleFood_sumsCorrectly() {
        val foods = listOf(
            FoodItem("1", "Egg", 1.0, "unit", 70.0, 6.0, 0.5, 5.0, 0.9f)
        )
        val totals = computeTotals(foods)
        assertEquals(70.0, totals.calories, 0.0)
        assertEquals(6.0, totals.proteinG, 0.0)
        assertEquals(0.5, totals.carbsG, 0.0)
        assertEquals(5.0, totals.fatG, 0.0)
    }

    @Test
    fun multipleFoods_sumsAllMacros() {
        val foods = listOf(
            FoodItem("1", "Rice", 1.0, "cup", 200.0, 4.0, 44.0, 0.4, null),
            FoodItem("2", "Chicken", 150.0, "g", 250.0, 35.0, 0.0, 10.0, null),
            FoodItem("3", "Broccoli", 1.0, "cup", 55.0, 3.6, 11.0, 0.6, null)
        )
        val totals = computeTotals(foods)
        assertEquals(505.0, totals.calories, 0.0)
        assertEquals(42.6, totals.proteinG, 0.01)
        assertEquals(55.0, totals.carbsG, 0.01)
        assertEquals(11.0, totals.fatG, 0.01)
    }
}
