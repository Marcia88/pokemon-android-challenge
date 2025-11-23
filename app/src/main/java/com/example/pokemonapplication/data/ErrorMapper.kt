package com.example.pokemonapplication.data

import com.example.pokemonapplication.data.datasources.HttpStatusException
import com.example.pokemonapplication.domain.DomainError

object ErrorMapper {
    fun map(throwable: Throwable): DomainError = when (throwable) {
        is HttpStatusException -> DomainError.Http(throwable.statusCode, throwable.message)
        is java.io.IOException -> DomainError.Network
        else -> DomainError.Unknown(throwable)
    }
}

