package com.calorieai.app.data

import com.calorieai.app.domain.model.MealAnalysisResult
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Holds the latest meal analysis result for the add-meal flow.
 * Analysis loading screen sets it on success; meal result screen reads it.
 */
@Singleton
class AnalysisResultHolder @Inject constructor() {
    var lastResult: MealAnalysisResult? = null
        private set

    fun setResult(result: MealAnalysisResult) {
        lastResult = result
    }

    fun clear() {
        lastResult = null
    }
}
