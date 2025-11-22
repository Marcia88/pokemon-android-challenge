package com.example.pokemonapplication.data.mapper

import com.example.pokemonapplication.data.dto.PokemonItemResponse
import com.example.pokemonapplication.data.dto.PokemonListResponse
import com.example.pokemonapplication.domain.model.PokemonListModel
import com.example.pokemonapplication.domain.model.PokemonModel
import javax.inject.Inject

class PokemonListMapper @Inject constructor() {
    fun mapToDomain(response: PokemonListResponse): PokemonListModel {
        return PokemonListModel(
            count = response.count,
            next = response.next,
            results = mapToPokemonItemList(response.results),
            previous = response.previous
        )
    }

    private fun mapToPokemonItemList(results: List<PokemonItemResponse>): List<PokemonModel> {
        return results.map { mapToPokemonModel(it) }
    }

    private fun mapToPokemonModel(response: PokemonItemResponse): PokemonModel {
        return PokemonModel(
            name = response.name,
            url = response.url
        )
    }
}