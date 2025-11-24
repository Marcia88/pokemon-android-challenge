package com.example.pokemonapplication.data.repositories.detail

import com.example.pokemonapplication.data.datasources.PokemonDetailService
import com.example.pokemonapplication.data.mapper.PokemonDetailMapper
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import kotlin.Result
import kotlinx.coroutines.withContext
import com.example.pokemonapplication.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PokemonDetailRemoteRepository @Inject constructor(
    private val pokemonDetailService: PokemonDetailService,
    private val mapper: PokemonDetailMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PokemonDetailRepository {

    override suspend fun getPokemonDetail(pokemon: String): Result<PokemonDetailModel> {
        val result = withContext(ioDispatcher) {
            pokemonDetailService.getPokemonDetail(pokemon)
        }
        return result.fold(
            onSuccess = { response -> Result.success(mapper.mapToDomain(response)) },
            onFailure = { throwable -> Result.failure(throwable) }
        )
    }
}