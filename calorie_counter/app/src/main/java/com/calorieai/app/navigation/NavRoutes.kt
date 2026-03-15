package com.calorieai.app.navigation

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

    /** Route with image URI (base64-encoded in path). Use after camera capture. */
    fun photoReviewWithImage(imageUri: String): String {
        val encoded = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(imageUri.toByteArray(UTF_8))
        return "photo_review/$encoded"
    }
}
