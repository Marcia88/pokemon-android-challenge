package com.example.pokemonapplication.presentation.ui.pokemon_detail.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.pokemonapplication.R
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.presentation.ui.pokemon_card.PokemonProvider

@Composable
fun PokemonDetailAboutTab(pokemonDetailModel: PokemonDetailModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .background(
                color = pokemonDetailModel.types.firstOrNull()?.type?.backgroundColor() ?: MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column {
            PokemonDetailTabField(
                stringResource(id = R.string.label_species),
                pokemonDetailModel.species
            )
            PokemonDetailTabField(
                stringResource(id = R.string.label_height),
                "${pokemonDetailModel.height}"
            )
            PokemonDetailTabField(
                stringResource(id = R.string.label_weight),
                "${pokemonDetailModel.weight}"
            )
            PokemonDetailTabField(
                stringResource(id = R.string.label_abilities),
                pokemonDetailModel.abilities
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonDetailAboutTabPreview(@PreviewParameter(PokemonProvider::class) pokemonDetail: PokemonDetailModel) {
    PokemonDetailAboutTab(pokemonDetailModel = pokemonDetail)
}