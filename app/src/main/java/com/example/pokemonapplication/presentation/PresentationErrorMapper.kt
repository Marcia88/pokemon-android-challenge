package com.example.pokemonapplication.presentation

import com.example.pokemonapplication.domain.DomainError

object PresentationErrorMapper {
    fun toUserMessage(error: DomainError): String = when (error) {
        is DomainError.Network -> "Network error. Please check your connection."
        is DomainError.Http -> when (error.statusCode) {
            in 400..499 -> "Client error (${error.statusCode}). Please check your request."
            in 500..599 -> "Server error (${error.statusCode}). Please try again later."
            else -> "HTTP error (${error.statusCode})."
        }
        is DomainError.Unknown -> "Unknown error"
    }
}
