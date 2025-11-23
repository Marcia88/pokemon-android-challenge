package com.example.pokemonapplication.data.repositories.search

import com.example.pokemonapplication.data.local.dao.SearchDao
import com.example.pokemonapplication.data.local.entity.CachedSearchEntity
import com.example.pokemonapplication.data.local.entity.SavedSearchEntity
import com.example.pokemonapplication.domain.model.PokemonListModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class SearchCacheRepositoryImpl @Inject constructor(
    private val dao: SearchDao
) : SearchCacheRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private fun toNoSpacesLowercaseKey(query: String) = query.trim().lowercase().replace("\\s+".toRegex(), "")

    override suspend fun saveQuery(query: String) = withContext(Dispatchers.IO) {
        val normalized = toNoSpacesLowercaseKey(query)
        val entity = SavedSearchEntity(query = normalized)
        dao.insertSavedSearch(entity)
    }

    override suspend fun getRecentQueries(limit: Int): List<String> = withContext(Dispatchers.IO) {
        dao.getRecentSavedSearches(limit).map { it.query }
    }

    override suspend fun saveCachedResult(query: String, model: PokemonListModel) = withContext(Dispatchers.IO) {
        val serialized = json.encodeToString(model)
        val normalized = toNoSpacesLowercaseKey(query)
        val entity = CachedSearchEntity(query = normalized, responseJson = serialized)
        dao.insertCachedSearch(entity)
    }

    override suspend fun getCachedResult(query: String): PokemonListModel? = withContext(Dispatchers.IO) {
        val normalized = toNoSpacesLowercaseKey(query)
        val entity = dao.getCachedSearch(normalized) ?: return@withContext null
        return@withContext try {
            json.decodeFromString<PokemonListModel>(entity.responseJson)
        } catch (_: Exception) {
            null
        }
    }
}
