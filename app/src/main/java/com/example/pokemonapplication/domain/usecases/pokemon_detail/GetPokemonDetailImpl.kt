package com.example.pokemonapplication.domain.usecases.pokemon_detail

import com.example.pokemonapplication.data.repositories.detail.PokemonDetailRepository
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import kotlin.Result
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.example.pokemonapplication.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher

class GetPokemonDetailImpl @Inject constructor(
    private val pokemonDetailRepository: PokemonDetailRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GetPokemonDetail {

    override fun getPokemonDetail(pokemon: String): Flow<Result<PokemonDetailModel>> = flow {
        val result = pokemonDetailRepository.getPokemonDetail(pokemon)
        emit(result)
    }.flowOn(ioDispatcher)
}
