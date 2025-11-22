package com.example.pokemonapplication.data.datasources

import com.example.pokemonapplication.data.dto.PokemonListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject
import javax.inject.Singleton

private const val QUERY_PARAM_LIMIT = "limit"
private const val QUERY_PARAM_OFFSET = "offset"
private const val DEFAULT_LIMIT = 10
private const val DEFAULT_OFFSET = 0

interface PokemonListService {
    suspend fun getPokemonList(limit: Int = DEFAULT_LIMIT, offset: Int = DEFAULT_OFFSET): Result<PokemonListResponse>
}

@Singleton
class PokemonListServiceImpl @Inject constructor(private val client: HttpClient) : PokemonListService {
    private val baseUrl = "https://pokeapi.co/api/v2"

    override suspend fun getPokemonList(limit: Int, offset: Int): Result<PokemonListResponse> {
        val url = "$baseUrl/pokemon"
        return queryApiHandlingExceptions {
            val response: PokemonListResponse = client.get(url) {
                parameter(QUERY_PARAM_LIMIT, limit)
                parameter(QUERY_PARAM_OFFSET, offset)
            }.body()
            response
        }
    }
}
