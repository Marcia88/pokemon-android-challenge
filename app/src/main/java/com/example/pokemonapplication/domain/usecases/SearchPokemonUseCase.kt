package com.example.pokemonapplication.domain.usecases

import com.example.pokemonapplication.domain.model.PokemonListModel
import com.example.pokemonapplication.domain.model.PokemonModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first

const val MAX_ATTEMPTS = 2

class SearchPokemonUseCase @Inject constructor(
    private val getPokemonList: GetPokemonList
) {
    suspend fun search(query: String, limit: Int): Result<PokemonListModel> {
        if (query.isBlank()) return Result.failure(IllegalArgumentException("query blank"))

        val matches = mutableListOf<PokemonModel>()
        var currentOffset = 0
        var keepLoading = true
        var attempts = 0

        while (keepLoading && matches.size < limit) {
            attempts++
            if (attempts > MAX_ATTEMPTS) break

            val pageResult: Result<PokemonListModel> = try {
                getPokemonList.getPokemonList(limit, currentOffset).first()
            } catch (e: Exception) {
                return Result.failure(e)
            }

            if (pageResult.isFailure) return pageResult

            val data = pageResult.getOrNull() ?: break
            val filtered = data.results.filter { it.name.contains(query, ignoreCase = true) }
            matches.addAll(filtered)
            currentOffset += limit

            keepLoading = matches.size < limit && (data.next != null && data.results.isNotEmpty())
        }

        val finalList = matches.take(limit)
        val model = PokemonListModel(count = finalList.size, next = null, results = finalList, previous = null)
        return Result.success(model)
    }
}
