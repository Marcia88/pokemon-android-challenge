package com.example.pokemonapplication.data.mapper

import com.example.pokemonapplication.data.dto.PokemonDetailResponse
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import javax.inject.Inject

class PokemonDetailMapper @Inject constructor() {
    fun mapToDomain(response: PokemonDetailResponse): PokemonDetailModel {
        val types = response.types.sortedBy { it.slot }.map { it.type.name }
        val abilities = response.abilities.sortedBy { it.slot }.map { it.ability.name }
        val stats = response.stats.associate { it.stat.name to it.base_stat }
        return PokemonDetailModel(
            id = response.id,
            name = response.name,
            imageUrl = response.sprites.frontDefault,
            types = types,
            abilities = abilities,
            stats = stats
        )
    }
}

