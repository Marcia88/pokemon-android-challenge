package com.example.pokemonapplication.domain.model

data class PokemonDetailModel(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val types: List<String> = emptyList(),
    val abilities: List<String> = emptyList(),
    val stats: Map<String, Int> = emptyMap()
)

