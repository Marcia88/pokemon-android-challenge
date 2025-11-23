package com.example.pokemonapplication.data.repositories.search

import com.example.pokemonapplication.domain.model.PokemonListModel

interface SearchCacheRepository {
    suspend fun saveQuery(query: String)
    suspend fun getRecentQueries(limit: Int = 10): List<String>
    suspend fun saveCachedResult(query: String, model: PokemonListModel)
    suspend fun getCachedResult(query: String): PokemonListModel?
}

