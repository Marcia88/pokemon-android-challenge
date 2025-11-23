package com.example.pokemonapplication.presentation.ui.pokemon_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pokemonapplication.domain.model.PokemonDetailModel

@Composable
fun PokemonDetail(
    pokemonDetail: PokemonDetailModel?
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        ImageCarousel(
            pokemonDetail = pokemonDetail,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )

        Text(
            text = pokemonDetail?.name.orEmpty(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(12.dp)
        )
    }
}