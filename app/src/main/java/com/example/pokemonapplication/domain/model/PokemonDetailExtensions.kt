package com.example.pokemonapplication.domain.model

/**
 * Extension for PokemonDetailModel
 */
fun PokemonDetailModel.getHighResImages(): List<String> {
    val sprites = this.spritesModel ?: return emptyList()
    val list = mutableListOf<String>()

    // non-null, non-blank image URLs
    sprites.other?.officialArtworkModel?.frontDefault?.trim()?.takeIf { it.isNotEmpty() }?.let { list.add(it) }
    sprites.other?.homeModel?.frontDefault?.trim()?.takeIf { it.isNotEmpty() }?.let { list.add(it) }
    sprites.other?.homeModel?.frontShiny?.trim()?.takeIf { it.isNotEmpty() }?.let { list.add(it) }

    return list.distinct()
}
