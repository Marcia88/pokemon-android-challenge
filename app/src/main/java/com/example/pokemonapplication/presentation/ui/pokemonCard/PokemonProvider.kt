package com.example.pokemonapplication.presentation.ui.pokemonCard

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.pokemonapplication.domain.model.PokemonDetailModel

class PokemonProvider : PreviewParameterProvider<PokemonDetailModel> {
    override val values: Sequence<PokemonDetailModel>
        get() = sequenceOf(
            PokemonDetailModel(
                id = 1,
                name = "bulbasaur",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                types = listOf("grass", "poison"),
                abilities = listOf("overgrow", "chlorophyll"),
                stats = mapOf(
                    "hp" to 45,
                    "attack" to 49,
                    "defense" to 49,
                    "special-attack" to 65,
                    "special-defense" to 65,
                    "speed" to 45
                )
            ),
            PokemonDetailModel(
                id = 2,
                name = "ivysaur",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png",
                types = listOf("grass", "poison"),
                abilities = listOf("overgrow", "chlorophyll"),
                stats = mapOf(
                    "hp" to 60,
                    "attack" to 62,
                    "defense" to 63,
                    "special-attack" to 80,
                    "special-defense" to 80,
                    "speed" to 60
                )
            ),
            PokemonDetailModel(
                id = 3,
                name = "venusaur",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/3.png",
                types = listOf("grass", "poison"),
                abilities = listOf("overgrow", "chlorophyll"),
                stats = mapOf(
                    "hp" to 80,
                    "attack" to 82,
                    "defense" to 83,
                    "special-attack" to 100,
                    "special-defense" to 100,
                    "speed" to 80
                )
            ),
            PokemonDetailModel(
                id = 4,
                name = "charmander",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
                types = listOf("fire"),
                abilities = listOf("blaze", "solar-power"),
                stats = mapOf(
                    "hp" to 39,
                    "attack" to 52,
                    "defense" to 43,
                    "special-attack" to 60,
                    "special-defense" to 50,
                    "speed" to 65
                )
            )
        )
}