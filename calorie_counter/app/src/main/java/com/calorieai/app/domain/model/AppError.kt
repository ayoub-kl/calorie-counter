package com.calorieai.app.domain.model

import java.io.IOException

/**
 * Shared error model used across repository, ViewModel, and UI.
 * Standardizes error display and retry behavior.
 */
sealed interface AppError {
    val userMessage: String
    val recoverable: Boolean

    data object CameraUnavailable : AppError {
        override val userMessage: String = "Camera is not available."
        override val recoverable: Boolean = true
    }

    data object PermissionDenied : AppError {
        override val userMessage: String = "Camera permission is required to capture meal photos."
        override val recoverable: Boolean = true
    }

    data class NetworkFailure(
        override val userMessage: String = "Network unavailable. Check your connection and retry.",
        override val recoverable: Boolean = true
    ) : AppError

    data class ApiError(
        override val userMessage: String = "Something went wrong. Please retry.",
        override val recoverable: Boolean = true
    ) : AppError

    data class InvalidAiResponse(
        override val userMessage: String = "Could not read analysis. Please try again.",
        override val recoverable: Boolean = true
    ) : AppError

    data object DatabaseError : AppError {
        override val userMessage: String = "Could not load or save data. Please retry."
        override val recoverable: Boolean = true
    }

    data object MissingResult : AppError {
        override val userMessage: String = "No meal result."
        override val recoverable: Boolean = false
    }

    data object InvalidMeal : AppError {
        override val userMessage: String = "Meal not found."
        override val recoverable: Boolean = false
    }

    companion object {
        /** Maps exceptions from analysis/API layer to a displayable [AppError]. */
        fun fromThrowable(t: Throwable): AppError = when (t) {
            is IOException -> NetworkFailure(
                userMessage = "Network unavailable. Check your connection and retry."
            )
            is IllegalArgumentException -> InvalidAiResponse(
                userMessage = t.message?.takeIf { it.isNotBlank() } ?: "Could not read analysis. Please try again."
            )
            else -> ApiError(
                userMessage = t.message?.takeIf { it.isNotBlank() } ?: "Something went wrong. Please retry."
            )
        }
    }
}
