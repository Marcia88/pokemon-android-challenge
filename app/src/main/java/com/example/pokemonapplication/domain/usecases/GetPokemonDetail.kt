package com.example.pokemonapplication.domain.usecases

import com.example.pokemonapplication.domain.model.PokemonDetailModel
import kotlin.Result
import kotlinx.coroutines.flow.Flow

interface GetPokemonDetail {
    fun getPokemonDetail(pokemon: String): Flow<Result<PokemonDetailModel>>
}
