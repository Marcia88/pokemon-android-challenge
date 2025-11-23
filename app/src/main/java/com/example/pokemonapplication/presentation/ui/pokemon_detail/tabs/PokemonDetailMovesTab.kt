package com.example.pokemonapplication.presentation.ui.pokemon_detail.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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

@Composable
fun PokemonDetailMovesTab(pokemonDetailModel: PokemonDetailModel) {
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
        if (pokemonDetailModel.moves.isEmpty()) {
            PokemonDetailTabField(
                stringResource(id = R.string.label_moves),
                stringResource(id = R.string.no_moves_available)
            )
        } else {
            val formattedMoves = pokemonDetailModel.moves.map { move ->
                move.replace('-', ' ').replaceFirstChar { it.uppercaseChar() }
            }
            PokemonDetailTabField(
                stringResource(id = R.string.label_moves),
                formattedMoves
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonDetailMovesTabPreview(@PreviewParameter(PokemonProvider::class) pokemon: PokemonDetailModel) {
    PokemonDetailMovesTab(pokemonDetailModel = pokemon)
}
