package com.example.pokemonapplication.data.datasources

import io.ktor.client.plugins.ResponseException

class HttpStatusException(val statusCode: Int, message: String?, cause: Throwable?) : RuntimeException(message, cause)

suspend fun <T> queryApiHandlingExceptions(apiCall: suspend () -> T): Result<T> {
    return try {
        Result.success(apiCall())
    } catch (e: Throwable) {
        val mapped = if (e is ResponseException) {
            val status = try { e.response.status.value } catch (_: Exception) { null }
            HttpStatusException(status ?: -1, "HTTP ${status ?: "?"}: ${e.message}", e)
        } else e
        return Result.failure(mapped)
    }
}
