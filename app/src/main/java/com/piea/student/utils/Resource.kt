package com.piea.student.utils

/**
 * Generic wrapper for UI state coming from repositories.
 */
sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    data object Idle : Resource<Nothing>()
}
