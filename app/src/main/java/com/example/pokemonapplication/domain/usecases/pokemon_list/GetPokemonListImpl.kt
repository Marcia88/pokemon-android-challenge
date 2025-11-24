package com.example.pokemonapplication.domain.usecases.pokemon_list

import com.example.pokemonapplication.data.repositories.pokemonlist.PokemonListRepository
import com.example.pokemonapplication.domain.model.PokemonListModel
import kotlin.Result
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.example.pokemonapplication.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher

class GetPokemonListImpl @Inject constructor(
    private val pokemonListRepository: PokemonListRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GetPokemonList {

    override fun getPokemonList(limit: Int, offset: Int): Flow<Result<PokemonListModel>> = flow {
        val result = pokemonListRepository.getPokemonList(limit, offset)
        emit(result)
    }.flowOn(ioDispatcher)
}