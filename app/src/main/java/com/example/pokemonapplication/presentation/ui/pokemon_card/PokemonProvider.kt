package com.example.pokemonapplication.presentation.ui.pokemon_card

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.domain.model.TypeModel
import com.example.pokemonapplication.domain.model.PokemonType
import com.example.pokemonapplication.domain.model.SpritesModel
import com.example.pokemonapplication.domain.model.OtherSpritesModel
import com.example.pokemonapplication.domain.model.OfficialArtworkModel
import com.example.pokemonapplication.domain.model.HomeModel

class PokemonProvider : PreviewParameterProvider<PokemonDetailModel> {
    override val values: Sequence<PokemonDetailModel>
        get() = sequenceOf(
            PokemonDetailModel(
                id = 1,
                name = "bulbasaur",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                types = listOf(
                    TypeModel(name = "grass", type = PokemonType.fromString("grass")),
                    TypeModel(name = "poison", type = PokemonType.fromString("poison"))
                ),
                abilities = listOf("overgrow", "chlorophyll"),
                moves = listOf("tackle", "vine-whip", "razor-leaf", "sleep-powder"),
                stats = mapOf(
                    "hp" to 45,
                    "attack" to 49,
                    "defense" to 49,
                    "special-attack" to 65,
                    "special-defense" to 65,
                    "speed" to 45
                ),
                spritesModel = SpritesModel(
                    other = OtherSpritesModel(
                        officialArtworkModel = OfficialArtworkModel(
                            frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
                        ),
                        homeModel = HomeModel(
                            frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/1.png",
                            frontShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/1.png"
                        )
                    )
                ),
                species = "bulbasaur",
                height = 7,
                weight = 69
            ),
            PokemonDetailModel(
                id = 2,
                name = "ivysaur",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png",
                types = listOf(
                    TypeModel(name = "grass", type = PokemonType.fromString("grass")),
                    TypeModel(name = "poison", type = PokemonType.fromString("poison"))
                ),
                abilities = listOf("overgrow", "chlorophyll"),
                moves = listOf("tackle", "vine-whip", "poison-powder", "sleep-powder"),
                stats = mapOf(
                    "hp" to 60,
                    "attack" to 62,
                    "defense" to 63,
                    "special-attack" to 80,
                    "special-defense" to 80,
                    "speed" to 60
                ),
                spritesModel = SpritesModel(
                    other = OtherSpritesModel(
                        officialArtworkModel = OfficialArtworkModel(
                            frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png"
                        ),
                        homeModel = HomeModel(
                            frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/2.png",
                            frontShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/2.png"
                        )
                    )
                ),
                species = "ivysaur",
                height = 10,
                weight = 130
            ),
            PokemonDetailModel(
                id = 3,
                name = "venusaur",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/3.png",
                types = listOf(TypeModel(name = "fire", type = PokemonType.fromString("fire"))),
                abilities = listOf("overgrow", "chlorophyll"),
                moves = listOf("solar-beam", "petal-blizzard", "earthquake"),
                stats = mapOf(
                    "hp" to 80,
                    "attack" to 82,
                    "defense" to 83,
                    "special-attack" to 100,
                    "special-defense" to 100,
                    "speed" to 80
                ),
                spritesModel = SpritesModel(
                    other = OtherSpritesModel(
                        officialArtworkModel = OfficialArtworkModel(
                            frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/3.png"
                        ),
                        homeModel = HomeModel(
                            frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/3.png",
                            frontShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/3.png"
                        )
                    )
                ),
                species = "venusaur",
                height = 20,
                weight = 1000
            ),
            PokemonDetailModel(
                id = 4,
                name = "charmander",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
                types = listOf(TypeModel(name = "fire", type = PokemonType.fromString("fire"))),
                abilities = listOf("blaze", "solar-power"),
                moves = listOf("scratch", "ember", "flamethrower"),
                stats = mapOf(
                    "hp" to 39,
                    "attack" to 52,
                    "defense" to 43,
                    "special-attack" to 60,
                    "special-defense" to 50,
                    "speed" to 65
                ),
                spritesModel = SpritesModel(
                    other = OtherSpritesModel(
                        officialArtworkModel = OfficialArtworkModel(
                            frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png"
                        ),
                        homeModel = HomeModel(
                            frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/4.png",
                            frontShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/4.png"
                        )
                    )
                ),
                species = "charmander",
                height = 6,
                weight = 85
            )
        )
}