package com.example.pokemonapplication.domain.model

data class PokemonListModel(
    val count: Int,
    val next: String?,
    val results: List<PokemonModel>,
    val previous: String?
)