package com.example.pokemonapplication.data.repositories.detail

import com.example.pokemonapplication.data.datasources.PokemonDetailService
import com.example.pokemonapplication.data.mapper.PokemonDetailMapper
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import kotlin.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonDetailRemoteRepository @Inject constructor(
    private val pokemonDetailService: PokemonDetailService,
    private val mapper: PokemonDetailMapper
) : PokemonDetailRepository {

    override suspend fun getPokemonDetail(pokemon: String): Result<PokemonDetailModel> {
        val result = withContext(Dispatchers.IO) {
            pokemonDetailService.getPokemonDetail(pokemon)
        }
        return result.fold(
            onSuccess = { response -> Result.success(mapper.mapToDomain(response)) },
            onFailure = { throwable -> Result.failure(throwable) }
        )
    }
}