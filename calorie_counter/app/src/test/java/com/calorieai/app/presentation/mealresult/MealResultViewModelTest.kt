package com.calorieai.app.presentation.mealresult

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.calorieai.app.data.AnalysisResultHolder
import com.calorieai.app.domain.model.AppError
import com.calorieai.app.domain.model.FoodItem
import com.calorieai.app.domain.model.MealAnalysisResult
import com.calorieai.app.domain.model.MealType
import com.calorieai.app.domain.model.NutritionTotals
import com.calorieai.app.domain.model.AnalysisStatus
import com.calorieai.app.data.repository.MealRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [MealResultViewModel] state and actions.
 * Mocks [AnalysisResultHolder] and [MealRepository]; isolates from UI.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MealResultViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val resultHolder = mockk<AnalysisResultHolder>(relaxed = true)
    private val mealRepository = mockk<MealRepository>(relaxed = true)

    private val sampleResult = MealAnalysisResult(
        analysisId = "aid-1",
        mealType = MealType.LUNCH,
        captureTimestamp = 1000L,
        imageUri = "content://photo",
        analysisStatus = AnalysisStatus.COMPLETED,
        foods = listOf(
            FoodItem("f1", "Rice", 1.0, "cup", 200.0, 4.0, 44.0, 0.4, 0.9f),
            FoodItem("f2", "Chicken", 100.0, "g", 250.0, 35.0, 0.0, 10.0, 0.85f)
        ),
        totals = NutritionTotals(450.0, 39.0, 44.0, 10.4),
        overallConfidence = 0.88f,
        userEdited = false
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mealRepository.observeAllMeals() } returns MutableStateFlow(emptyList())
        coEvery { mealRepository.observeMealById(any()) } returns MutableStateFlow(null)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_whenHolderHasNoResult_emitsErrorState() = runTest(testDispatcher) {
        coEvery { resultHolder.lastResult } returns null
        val viewModel = MealResultViewModel(resultHolder, mealRepository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(AppError.MissingResult, state.error)
            assertTrue(state.foods.isEmpty())
        }
    }

    @Test
    fun init_whenHolderHasResult_emitsContentState() = runTest(testDispatcher) {
        coEvery { resultHolder.lastResult } returns sampleResult
        val viewModel = MealResultViewModel(resultHolder, mealRepository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(null, state.error)
            assertEquals("aid-1", state.id)
            assertEquals(MealType.LUNCH, state.mealType)
            assertEquals(2, state.foods.size)
            assertEquals(450.0, state.totals.calories, 0.0)
            assertEquals(39.0, state.totals.proteinG, 0.0)
            assertFalse(state.userEdited)
        }
    }

    @Test
    fun removeFood_updatesTotalsAndSetsUserEdited() = runTest(testDispatcher) {
        coEvery { resultHolder.lastResult } returns sampleResult
        val viewModel = MealResultViewModel(resultHolder, mealRepository)

        viewModel.uiState.test {
            awaitItem()
            viewModel.removeFood("f1")
            val state = awaitItem()
            assertEquals(1, state.foods.size)
            assertEquals("Chicken", state.foods[0].name)
            assertEquals(250.0, state.totals.calories, 0.0)
            assertEquals(35.0, state.totals.proteinG, 0.0)
            assertTrue(state.userEdited)
        }
    }

    @Test
    fun addFood_addsNewItemAndUpdatesTotals() = runTest(testDispatcher) {
        coEvery { resultHolder.lastResult } returns sampleResult
        val viewModel = MealResultViewModel(resultHolder, mealRepository)

        viewModel.uiState.test {
            awaitItem()
            viewModel.addFood()
            val state = awaitItem()
            assertEquals(3, state.foods.size)
            assertTrue(state.foods.any { it.name == "New item" })
            assertTrue(state.userEdited)
        }
    }

    @Test
    fun updateFood_changesQuantityAndUnitAndRecalculatesTotals() = runTest(testDispatcher) {
        coEvery { resultHolder.lastResult } returns sampleResult
        val viewModel = MealResultViewModel(resultHolder, mealRepository)

        viewModel.uiState.test {
            awaitItem()
            viewModel.updateFood("f1", quantity = 2.0, unit = "cups")
            val state = awaitItem()
            val rice = state.foods.find { it.id == "f1" }
            assertEquals(2.0, rice!!.quantity, 0.0)
            assertEquals("cups", rice.unit)
            assertTrue(state.userEdited)
        }
    }

    @Test
    fun clearError_clearsErrorInState() = runTest(testDispatcher) {
        coEvery { resultHolder.lastResult } returns null
        val viewModel = MealResultViewModel(resultHolder, mealRepository)

        viewModel.uiState.test {
            awaitItem()
            viewModel.clearError()
            val state = awaitItem()
            assertEquals(null, state.error)
        }
    }

    @Test
    fun saveMeal_success_emitsSaveSuccess() = runTest(testDispatcher) {
        coEvery { resultHolder.lastResult } returns sampleResult
        coEvery { mealRepository.saveMeal(any()) } returns Unit
        val viewModel = MealResultViewModel(resultHolder, mealRepository)

        viewModel.saveSuccess.test {
            viewModel.saveMeal()
            awaitItem()
        }
    }

    @Test
    fun saveMeal_whenNoFoods_doesNotEmitSaveSuccess() = runTest(testDispatcher) {
        val resultWithNoFoods = sampleResult.copy(
            foods = emptyList(),
            totals = NutritionTotals(0.0, 0.0, 0.0, 0.0)
        )
        coEvery { resultHolder.lastResult } returns resultWithNoFoods
        val viewModel = MealResultViewModel(resultHolder, mealRepository)

        viewModel.saveSuccess.test {
            viewModel.saveMeal()
            // No item should be emitted because foods are empty
            expectNoEvents()
        }
    }
}
