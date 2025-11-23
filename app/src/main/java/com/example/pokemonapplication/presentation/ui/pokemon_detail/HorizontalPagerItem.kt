package com.example.pokemonapplication.presentation.ui.pokemon_detail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.presentation.ui.pokemon_card.PokemonProvider

@Composable
fun HorizontalPagerItem(
    onImageTap: ((String) -> Unit)?,
    url: String,
    pokemonDetail: PokemonDetailModel?
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(24.dp)
            .height(220.dp)
            .fillMaxWidth()
            .animateContentSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    onImageTap?.invoke(url)
                })
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                AsyncImage(
                    model = url,
                    contentDescription = "pokemon image",
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier.size(180.dp)
                )
            }

            Column {
                Text(
                    text = pokemonDetail?.name.orEmpty().uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background
                )
                pokemonDetail?.types?.let { types ->
                    LazyRow(
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        items(count = types.size, key = { it }) { index ->
                            val type = types[index]
                            TypeBubble(type)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HorizontalPagerItemPreview(@PreviewParameter(PokemonProvider::class) pokemonDetail: PokemonDetailModel?) {
    HorizontalPagerItem(
        onImageTap = {},
        url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
        pokemonDetail = pokemonDetail
    )
}