package com.example.pokemonapplication.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponse(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<PokemonItemResponse> = emptyList()
)