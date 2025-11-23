package com.example.pokemonapplication.presentation.ui.pokemon_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.presentation.ui.pokemon_detail.tabs.PokemonDetailTabs
import com.example.pokemonapplication.presentation.ui.pokemon_card.PokemonProvider

@Composable
fun PokemonDetailScreen(
    pokemonDetail: PokemonDetailModel?
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        ImageCarousel(
            pokemonDetail = pokemonDetail,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )

        pokemonDetail?.let { PokemonDetailTabs(it) }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonDetailScreenPreview(@PreviewParameter(PokemonProvider::class) pokemonDetail: PokemonDetailModel) {
    PokemonDetailScreen(pokemonDetail = pokemonDetail)
}
