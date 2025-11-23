package com.example.pokemonapplication.domain

sealed class DomainError {
    object Network : DomainError()
    data class Http(val statusCode: Int, val message: String? = null) : DomainError()
    data class Unknown(val cause: Throwable? = null) : DomainError()
}

