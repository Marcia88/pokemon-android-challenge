package com.example.pokemonapplication.presentation.ui.pokemon_list

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.pokemonapplication.domain.model.PokemonModel

class PokemonListProvider : PreviewParameterProvider<List<PokemonModel>> {
    override val values: Sequence<List<PokemonModel>>
        get() = sequenceOf(
            listOf(
                PokemonModel(
                    name = "bulbasaur",
                    url = "https://pokeapi.co/api/v2/pokemon/1/"
                ),
                PokemonModel(
                    name = "ivysaur",
                    url = "https://pokeapi.co/api/v2/pokemon/2/"
                ),
                PokemonModel(
                    name = "venusaur",
                    url = "https://pokeapi.co/api/v2/pokemon/3/"
                ),
                PokemonModel(
                    name = "charmander",
                    url = "https://pokeapi.co/api/v2/pokemon/4/"
                )
            )
        )
}