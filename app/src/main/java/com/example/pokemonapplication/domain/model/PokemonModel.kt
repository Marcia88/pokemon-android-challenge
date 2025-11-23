package com.example.pokemonapplication.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonModel(
    val name: String,
    val url: String
)