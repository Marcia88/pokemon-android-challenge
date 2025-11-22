package com.example.pokemonapplication.data.repositories.pokemonlist

import com.example.pokemonapplication.data.datasources.PokemonListService
import com.example.pokemonapplication.data.mapper.PokemonListMapper
import com.example.pokemonapplication.domain.model.PokemonListModel
import kotlin.Result
import javax.inject.Inject

class PokemonListRemoteRepository @Inject constructor(
    private val pokemonListService: PokemonListService,
    private val mapper: PokemonListMapper
): PokemonListRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): Result<PokemonListModel> {
        val result = pokemonListService.getPokemonList(limit, offset)
        return result.fold(
            onSuccess = { response -> Result.success(mapper.mapToDomain(response)) },
            onFailure = { throwable -> Result.failure(throwable) }
        )
    }
}