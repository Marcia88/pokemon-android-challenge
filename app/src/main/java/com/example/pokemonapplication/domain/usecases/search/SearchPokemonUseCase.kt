package com.example.pokemonapplication.domain.usecases.search

import com.example.pokemonapplication.domain.model.PokemonListModel
import kotlinx.coroutines.flow.Flow

interface SearchPokemonUseCase {
    fun search(
        query: String,
        limit: Int,
        currentData: PokemonListModel? = null
    ): Flow<Result<PokemonListModel>>
}