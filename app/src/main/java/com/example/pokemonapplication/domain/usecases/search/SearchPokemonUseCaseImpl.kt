package com.example.pokemonapplication.domain.usecases.search

import com.example.pokemonapplication.domain.model.PokemonListModel
import com.example.pokemonapplication.domain.model.PokemonModel
import com.example.pokemonapplication.data.repositories.search.SearchCacheRepository
import com.example.pokemonapplication.domain.usecases.pokemon_list.GetPokemonList
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

const val MAX_ATTEMPTS = 2

class SearchPokemonUseCaseImpl @Inject constructor(
    private val getPokemonList: GetPokemonList,
    private val cacheRepository: SearchCacheRepository
) : SearchPokemonUseCase {
    private fun toNoSpacesLowercaseKey(query: String) =
        query.trim().lowercase().replace("\\s+".toRegex(), "")

    private fun filterByQuery(results: List<PokemonModel>, query: String) =
        results.filter { it.name.contains(query, ignoreCase = true) }

    private fun buildListModel(results: List<PokemonModel>) =
        PokemonListModel(count = results.size, next = null, results = results, previous = null)

    private suspend fun saveCacheSafe(key: String, model: PokemonListModel) {
        try { cacheRepository.saveCachedResult(key, model) } catch (_: Exception) { /* ignore */ }
        try { cacheRepository.saveQuery(key) } catch (_: Exception) { /* ignore */ }
    }

    private suspend fun getCachedSafe(key: String): PokemonListModel? = try {
        cacheRepository.getCachedResult(key)
    } catch (_: Exception) { null }

    override fun search(
        query: String,
        limit: Int,
        currentData: PokemonListModel?
    ): Flow<Result<PokemonListModel>> = flow {
        val key = toNoSpacesLowercaseKey(query)

        // If currentData is provided, emit filtered result and cache it
        if (currentData != null) {
            val filteredNow = filterByQuery(currentData.results, query)
            if (filteredNow.isNotEmpty()) {
                val modelNow = buildListModel(filteredNow)
                emit(Result.success(modelNow))
                saveCacheSafe(key, modelNow)
            }
        }

        // Try from API -> up to MAX_ATTEMPTS
        val matches = mutableListOf<PokemonModel>()
        var currentOffset = 0
        var attempts = 0

        while (matches.size < limit) {
            attempts++
            if (attempts > MAX_ATTEMPTS) break

            val pageResult: Result<PokemonListModel> =
                getPokemonList.getPokemonList(limit, currentOffset).first()

            if (pageResult.isFailure) {
                // API reported failure -> fallback to cache
                val ex = pageResult.exceptionOrNull() ?: Exception("Unknown API error")
                val cached = getCachedSafe(key)
                if (cached != null) {
                    emit(Result.success(cached))
                } else {
                    emit(Result.failure(ex))
                }
                return@flow
            } else {
                val data = pageResult.getOrNull() ?: break
                val filtered = filterByQuery(data.results, query)
                matches.addAll(filtered)

                // If we have any matches, return the list
                if (matches.isNotEmpty()) {
                    val finalList = matches.take(limit)
                    val model = buildListModel(finalList)
                    saveCacheSafe(key, model)
                    emit(Result.success(model))
                    return@flow
                }

                // No matches on this page and no more pages -> emit empty result
                if (data.next.isNullOrEmpty() || data.results.isEmpty()) {
                    val emptyModel = buildListModel(emptyList())
                    saveCacheSafe(key, emptyModel)
                    emit(Result.success(emptyModel))
                    return@flow
                }
            }

            currentOffset += limit
        }
    }.catch { e ->
        emit(Result.failure(e))
    }
}
