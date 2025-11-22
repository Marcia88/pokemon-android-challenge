package com.example.pokemonapplication.data.repositories.pokemonlist

import com.example.pokemonapplication.domain.model.PokemonListModel
import kotlin.Result

interface PokemonListRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Result<PokemonListModel>
}