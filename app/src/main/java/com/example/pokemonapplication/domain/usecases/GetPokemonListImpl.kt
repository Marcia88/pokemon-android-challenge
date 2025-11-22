package com.example.pokemonapplication.domain.usecases

import com.example.pokemonapplication.data.repositories.pokemonlist.PokemonListRepository
import com.example.pokemonapplication.domain.model.PokemonListModel
import kotlin.Result
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers

class GetPokemonListImpl @Inject constructor(
    private val pokemonListRepository: PokemonListRepository
) : GetPokemonList {

    override fun getPokemonList(): Flow<Result<PokemonListModel>> = flow {
        val result = pokemonListRepository.getPokemonList(10, 0)
        emit(result)
    }.flowOn(Dispatchers.IO)
}