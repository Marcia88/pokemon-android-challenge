package com.example.pokemonapplication.domain.usecases

import com.example.pokemonapplication.domain.model.PokemonListModel
import kotlin.Result
import kotlinx.coroutines.flow.Flow

interface GetPokemonList {
    fun getPokemonList(): Flow<Result<PokemonListModel>>
}