package com.example.pokemonapplication.data.mapper

import com.example.pokemonapplication.data.dto.PokemonDetailResponse
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.domain.model.SpritesModel
import com.example.pokemonapplication.domain.model.OtherSpritesModel
import com.example.pokemonapplication.domain.model.OfficialArtworkModel
import com.example.pokemonapplication.domain.model.HomeModel
import com.example.pokemonapplication.domain.model.DreamWorldModel
import com.example.pokemonapplication.domain.model.TypeModel
import com.example.pokemonapplication.domain.model.PokemonType
import javax.inject.Inject

class PokemonDetailMapper @Inject constructor() {
    fun mapToDomain(response: PokemonDetailResponse): PokemonDetailModel {
        val types = response.types.sortedBy { it.slot }.map { slot ->
            val typeName = slot.type.name
            TypeModel(
                name = typeName,
                type = PokemonType.fromString(typeName)
            )
        }
        val abilities = response.abilities.sortedBy { it.slot }.map { it.ability.name }
        val stats = response.stats.associate { it.stat.name to it.baseStat }
        val moves = response.moves.map { it.move.name }

        val other = response.sprites.other
        val domainOther = if (other != null) {
            OtherSpritesModel(
                officialArtworkModel = other.officialArtwork?.let { OfficialArtworkModel(frontDefault = it.frontDefault) },
                homeModel = other.home?.let { HomeModel(frontDefault = it.frontDefault, frontShiny = it.frontShiny) },
                dreamWorldModel = other.dreamWorld?.let { DreamWorldModel(frontDefault = it.frontDefault) }
            )
        } else {
            null
        }

        val spritesModel = SpritesModel(
            frontDefault = response.sprites.frontDefault,
            other = domainOther
        )

        return PokemonDetailModel(
            id = response.id,
            name = response.name,
            imageUrl = response.sprites.frontDefault,
            types = types,
            abilities = abilities,
            moves = moves,
            stats = stats,
            spritesModel = spritesModel,
            species = response.species.name,
            height = response.height,
            weight = response.weight
        )
    }
}
