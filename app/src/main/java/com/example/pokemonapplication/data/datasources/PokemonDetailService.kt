package com.example.pokemonapplication.data.datasources

import com.example.pokemonapplication.data.dto.PokemonDetailResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

interface PokemonDetailService {
    suspend fun getPokemonDetail(pokemon: String): Result<PokemonDetailResponse>
}

@Singleton
class PokemonDetailServiceImpl @Inject constructor(private val client: HttpClient) : PokemonDetailService {
    private val baseUrl = "https://pokeapi.co/api/v2"

    override suspend fun getPokemonDetail(pokemon: String): Result<PokemonDetailResponse> {
        val url = "$baseUrl/pokemon/$pokemon"
        return queryApiHandlingExceptions {
            val response: PokemonDetailResponse = client.get(url).body()
            response
        }
    }
}
