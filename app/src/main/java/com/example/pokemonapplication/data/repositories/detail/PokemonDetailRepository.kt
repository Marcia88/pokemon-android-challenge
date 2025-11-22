package com.example.pokemonapplication.data.repositories.detail

import com.example.pokemonapplication.domain.model.PokemonDetailModel
import kotlin.Result

interface PokemonDetailRepository {
    suspend fun getPokemonDetail(pokemon: String): Result<PokemonDetailModel>
}