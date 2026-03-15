package com.calorieai.app.data.api

import com.calorieai.app.domain.model.MealType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for DTO -> domain mapping in [AnalysisMapper].
 */
class AnalysisMapperTest {

    @Test
    fun toDomain_withValidDto_mapsToMealAnalysisResult() {
        val dto = AnalysisResponseDto(
            analysisId = "aid-1",
            overallConfidence = 0.85,
            foods = listOf(
                FoodItemDto(
                    name = "Rice",
                    quantity = 1.0,
                    unit = "cup",
                    calories = 200.0,
                    protein_g = 4.0,
                    carbs_g = 44.0,
                    fat_g = 0.4,
                    confidence = 0.9
                )
            ),
            totals = TotalsDto(200.0, 4.0, 44.0, 0.4)
        )
        val result = AnalysisMapper.toDomain(
            dto = dto,
            mealType = MealType.LUNCH,
            captureTimestamp = 1000L,
            imageUri = "content://photo"
        )
        assertTrue(result.isSuccess)
        val domain = result.getOrNull()!!
        assertEquals("aid-1", domain.analysisId)
        assertEquals(MealType.LUNCH, domain.mealType)
        assertEquals(1000L, domain.captureTimestamp)
        assertEquals("content://photo", domain.imageUri)
        assertEquals(1, domain.foods.size)
        assertEquals("Rice", domain.foods[0].name)
        assertEquals(1.0, domain.foods[0].quantity, 0.0)
        assertEquals("cup", domain.foods[0].unit)
        assertEquals(200.0, domain.totals.calories, 0.0)
        assertEquals(4.0, domain.totals.proteinG, 0.0)
        assertEquals(44.0, domain.totals.carbsG, 0.0)
        assertEquals(0.4, domain.totals.fatG, 0.0)
        assertNotNull(domain.overallConfidence)
        assertEquals(0.85f, domain.overallConfidence!!, 0.001f)
        assertEquals(false, domain.userEdited)
    }

    @Test
    fun toDomain_withEmptyFoodName_skipsFoodItem() {
        val dto = AnalysisResponseDto(
            foods = listOf(
                FoodItemDto(name = "  ", quantity = 1.0, unit = "cup", calories = 100.0),
                FoodItemDto(name = "Bread", quantity = 1.0, unit = "slice", calories = 80.0)
            ),
            totals = TotalsDto(180.0, 0.0, 0.0, 0.0)
        )
        val result = AnalysisMapper.toDomain(dto, MealType.BREAKFAST, 0L, null)
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()!!.foods.size)
        assertEquals("Bread", result.getOrNull()!!.foods[0].name)
    }

    @Test
    fun toDomain_withNullFoods_usesEmptyListAndTotalsFromDto() {
        val dto = AnalysisResponseDto(
            totals = TotalsDto(350.0, 15.0, 40.0, 12.0)
        )
        val result = AnalysisMapper.toDomain(dto, MealType.DINNER, 2000L, null)
        assertTrue(result.isSuccess)
        val domain = result.getOrNull()!!
        assertTrue(domain.foods.isEmpty())
        assertEquals(350.0, domain.totals.calories, 0.0)
        assertEquals(15.0, domain.totals.proteinG, 0.0)
        assertEquals(40.0, domain.totals.carbsG, 0.0)
        assertEquals(12.0, domain.totals.fatG, 0.0)
    }

    @Test
    fun toDomain_withMissingTotals_computesFromFoodItems() {
        val dto = AnalysisResponseDto(
            foods = listOf(
                FoodItemDto(name = "A", calories = 100.0, protein_g = 2.0, carbs_g = 10.0, fat_g = 1.0),
                FoodItemDto(name = "B", calories = 150.0, protein_g = 5.0, carbs_g = 15.0, fat_g = 2.0)
            ),
            totals = null
        )
        val result = AnalysisMapper.toDomain(dto, MealType.SNACK, 0L, null)
        assertTrue(result.isSuccess)
        val domain = result.getOrNull()!!
        assertEquals(250.0, domain.totals.calories, 0.0)
        assertEquals(7.0, domain.totals.proteinG, 0.0)
        assertEquals(25.0, domain.totals.carbsG, 0.0)
        assertEquals(3.0, domain.totals.fatG, 0.0)
    }

    @Test
    fun toDomain_normalizesNegativeValuesToZero() {
        val dto = AnalysisResponseDto(
            foods = listOf(
                FoodItemDto(name = "X", calories = -50.0, protein_g = -1.0, carbs_g = 0.0, fat_g = 0.0)
            ),
            totals = TotalsDto(-10.0, 0.0, 0.0, 0.0)
        )
        val result = AnalysisMapper.toDomain(dto, MealType.LUNCH, 0L, null)
        assertTrue(result.isSuccess)
        val domain = result.getOrNull()!!
        assertEquals(0.0, domain.foods[0].calories, 0.0)
        assertEquals(0.0, domain.foods[0].proteinG, 0.0)
        assertEquals(0.0, domain.totals.calories, 0.0)
    }

    @Test
    fun toDomain_clampsConfidenceToValidRange() {
        val dto = AnalysisResponseDto(
            overallConfidence = 1.5,
            foods = listOf(
                FoodItemDto(name = "Y", confidence = -0.2)
            ),
            totals = TotalsDto(0.0, 0.0, 0.0, 0.0)
        )
        val result = AnalysisMapper.toDomain(dto, MealType.BREAKFAST, 0L, null)
        assertTrue(result.isSuccess)
        val domain = result.getOrNull()!!
        assertEquals(1.0f, domain.overallConfidence!!, 0.001f)
        assertEquals(0.0f, domain.foods[0].confidence!!, 0.001f)
    }

    @Test
    fun toDomain_withBlankAnalysisId_generatesNewId() {
        val dto = AnalysisResponseDto(analysisId = "", totals = TotalsDto(0.0, 0.0, 0.0, 0.0))
        val result = AnalysisMapper.toDomain(dto, MealType.LUNCH, 0L, null)
        assertTrue(result.isSuccess)
        val id = result.getOrNull()!!.analysisId
        assertTrue(id.isNotBlank())
        assertTrue(id.length >= 10)
    }

    @Test
    fun toDomain_defaultsUnitToServingWhenBlank() {
        val dto = AnalysisResponseDto(
            foods = listOf(FoodItemDto(name = "Z", unit = null, calories = 0.0)),
            totals = TotalsDto(0.0, 0.0, 0.0, 0.0)
        )
        val result = AnalysisMapper.toDomain(dto, MealType.DINNER, 0L, null)
        assertTrue(result.isSuccess)
        assertEquals("serving", result.getOrNull()!!.foods[0].unit)
    }

    @Test
    fun toDomain_nullOverallConfidence_remainsNull() {
        val dto = AnalysisResponseDto(totals = TotalsDto(0.0, 0.0, 0.0, 0.0))
        val result = AnalysisMapper.toDomain(dto, MealType.SNACK, 0L, null)
        assertTrue(result.isSuccess)
        assertNull(result.getOrNull()!!.overallConfidence)
    }
}
