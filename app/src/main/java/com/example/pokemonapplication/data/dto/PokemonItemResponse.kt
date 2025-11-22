package com.example.pokemonapplication.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PokemonItemResponse(
    val name: String,
    val url: String
)