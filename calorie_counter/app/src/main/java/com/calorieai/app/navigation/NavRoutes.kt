package com.calorieai.app.navigation

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8

/**
 * Stable route names for the app. Pass only minimal arguments (IDs, URIs).
 * Do not pass bitmaps or heavy objects through navigation.
 */
object NavRoutes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val MEAL_TYPE_SELECT = "meal_type_select"
    const val CAMERA_CAPTURE = "camera_capture"
    const val PHOTO_REVIEW = "photo_review"
    const val ANALYSIS_LOADING = "analysis_loading"
    const val MEAL_RESULT = "meal_result"
    const val MEAL_EDIT = "meal_edit"
    const val HISTORY = "history"
    const val MEAL_DETAIL = "meal_detail"
    const val SETTINGS = "settings"

    /** Route with mealId argument. Use for navigating to a specific meal. */
    fun mealDetail(mealId: String) = "meal_detail/$mealId"

    /** Route with meal type. Use when navigating from meal type selection to camera. */
    fun cameraCaptureWithMealType(mealType: String) = "camera_capture/$mealType"

    /** Route with meal type and URI-encoded image URI. Use after camera capture. */
    fun photoReviewWithImage(mealType: String, imageUri: String): String {
        val encoded = URLEncoder.encode(imageUri, UTF_8).replace("+", "%20")
        return "photo_review/$mealType/$encoded"
    }

    /** Route with meal type and URI-encoded image URI. Use when starting analysis. */
    fun analysisLoading(mealType: String, imageUri: String): String {
        val encoded = URLEncoder.encode(imageUri, UTF_8).replace("+", "%20")
        return "analysis_loading/$mealType/$encoded"
    }

    /** Decode imageUri read from navigation arguments or SavedStateHandle. */
    fun decodeImageUriFromRoute(encoded: String): String = try {
        if (encoded.isBlank()) "" else URLDecoder.decode(encoded, UTF_8)
    } catch (_: Exception) {
        ""
    }
}
