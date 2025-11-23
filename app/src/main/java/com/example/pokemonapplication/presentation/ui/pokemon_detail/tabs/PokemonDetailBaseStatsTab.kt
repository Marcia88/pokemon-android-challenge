package com.example.pokemonapplication.presentation.ui.pokemon_detail.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun PokemonDetailBaseStatsTab(pokemonDetailModel: PokemonDetailModel) {
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
            if (pokemonDetailModel.stats.isEmpty()) {
                PokemonDetailTabField(
                    stringResource(id = R.string.label_stats),
                    stringResource(id = R.string.no_stats_available)
                )
                return@Column
            }

            pokemonDetailModel.stats.forEach { (name, value) ->
                val fieldLabel = name.replaceFirstChar { it.uppercaseChar() } + ":"
                PokemonDetailTabField(fieldLabel, value.toString())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonDetailBaseStatsTabPreview(@PreviewParameter(PokemonProvider::class) pokemonDetail: PokemonDetailModel) {
    PokemonDetailBaseStatsTab(pokemonDetailModel = pokemonDetail)
}