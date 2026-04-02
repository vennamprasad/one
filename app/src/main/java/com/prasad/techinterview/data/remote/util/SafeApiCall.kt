package com.prasad.techinterview.data.remote.util

import retrofit2.Response

suspend fun <T, R> safeApiCall(
    apiCall: suspend () -> Response<T>,
    transform: (T) -> R,
): Result<R> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(transform(it))
            } ?: Result.failure(Exception("Response body is null"))
        } else {
            Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}