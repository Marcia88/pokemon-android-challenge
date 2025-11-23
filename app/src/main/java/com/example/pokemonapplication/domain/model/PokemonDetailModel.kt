package com.example.pokemonapplication.domain.model

data class PokemonDetailModel(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val types: List<TypeModel> = emptyList(),
    val abilities: List<String> = emptyList(),
    val stats: Map<String, Int> = emptyMap(),
    val spritesModel: SpritesModel? = null
)

data class TypeModel(
    val name: String? = null,
    val type: PokemonType? = null
)

data class SpritesModel(
    val frontDefault: String? = null,
    val other: OtherSpritesModel? = null
)

data class OtherSpritesModel(
    val officialArtworkModel: OfficialArtworkModel? = null,
    val homeModel: HomeModel? = null,
    val dreamWorldModel: DreamWorldModel? = null
)

data class OfficialArtworkModel(
    val frontDefault: String? = null
)

data class HomeModel(
    val frontDefault: String? = null,
    val frontShiny: String? = null
)

data class DreamWorldModel(
    val frontDefault: String? = null
)
