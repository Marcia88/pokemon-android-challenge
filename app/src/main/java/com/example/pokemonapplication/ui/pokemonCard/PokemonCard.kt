package com.example.pokemonapplication.ui.pokemonCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme

const val PLACEHOLDER = "..."

@Composable
fun PokemonCard(pokemonDetail: PokemonDetailModel?) {
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
                text = pokemonDetail?.name ?: PLACEHOLDER,
                style = MaterialTheme.typography.titleLarge
            )

            Box(contentAlignment = Alignment.Center) {
                if (pokemonDetail?.imageUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pokemonDetail.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = pokemonDetail.name,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(100.dp)
                    )
                } else {
                    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
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

@Preview
@Composable
fun PlaceholderPokemonCardPreview() {
    PokemonApplicationTheme {
        PokemonCard(null)
    }
}
