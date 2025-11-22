package com.example.pokemonapplication.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme

@Composable
fun PokemonCard(pokemonDetail: PokemonDetailModel) {
    Card(
        modifier = Modifier.size(150.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = pokemonDetail.name,
                style = MaterialTheme.typography.titleLarge
            )
            AsyncImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
                    .height(100.dp),
                model = pokemonDetail.imageUrl,
                contentDescription = pokemonDetail.name
            )
        }
    }
}

@Preview
@Composable
fun PokemonCardPreview(@PreviewParameter(PokemonProvider::class) pokemon: PokemonDetailModel) {
    PokemonApplicationTheme {
        PokemonCard(pokemon)
    }
}