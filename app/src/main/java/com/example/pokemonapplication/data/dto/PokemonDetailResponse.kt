package com.example.pokemonapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val sprites: SpritesResponse,
    val types: List<TypeSlotResponse> = emptyList(),
    val abilities: List<AbilitySlotResponse> = emptyList(),
    val stats: List<StatSlotResponse> = emptyList()
)

@Serializable
data class SpritesResponse(
    @SerialName("front_default") val frontDefault: String? = null
)

@Serializable
data class TypeSlotResponse(
    val slot: Int,
    val type: NamedResourceResponse
)

@Serializable
data class AbilitySlotResponse(
    val slot: Int,
    val ability: NamedResourceResponse
)

@Serializable
data class StatSlotResponse(
    val base_stat: Int,
    val effort: Int,
    val stat: NamedResourceResponse
)

@Serializable
data class NamedResourceResponse(
    val name: String,
    val url: String
)

