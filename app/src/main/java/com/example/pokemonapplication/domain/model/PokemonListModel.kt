package com.example.pokemonapplication.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonListModel(
    val count: Int,
    val next: String?,
    val results: List<PokemonModel>,
    val previous: String?
)