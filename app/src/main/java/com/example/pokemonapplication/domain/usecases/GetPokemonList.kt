package com.example.pokemonapplication.domain.usecases

import com.example.pokemonapplication.domain.model.PokemonListModel
import kotlin.Result
import kotlinx.coroutines.flow.Flow

const val ITEMS_PER_PAGE = 20

interface GetPokemonList {
    fun getPokemonList(limit: Int = ITEMS_PER_PAGE, offset: Int = 0): Flow<Result<PokemonListModel>>
}