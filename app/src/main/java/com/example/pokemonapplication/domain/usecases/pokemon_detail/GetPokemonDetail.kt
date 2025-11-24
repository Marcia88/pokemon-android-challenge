package com.example.pokemonapplication.domain.usecases.pokemon_detail

import com.example.pokemonapplication.domain.model.PokemonDetailModel
import kotlin.Result
import kotlinx.coroutines.flow.Flow

interface GetPokemonDetail {
    fun getPokemonDetail(pokemon: String): Flow<Result<PokemonDetailModel>>
}
