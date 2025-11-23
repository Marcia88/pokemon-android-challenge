package com.example.pokemonapplication.presentation.ui.pokemon_card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay

@Composable
fun PokemonImage(
    imageUrl: String?,
    pokemonName: String?,
    modifier: Modifier = Modifier,
    onShowTitleChange: (Boolean) -> Unit
) {
    if (!imageUrl.isNullOrEmpty()) {
        val context = LocalContext.current
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = pokemonName,
            modifier = modifier
        ) {
            val state = painter.state
            var showError by remember { mutableStateOf(false) }

            LaunchedEffect(state) {
                when (state) {
                    is AsyncImagePainter.State.Success -> {
                        onShowTitleChange(true)
                        showError = false
                    }

                    is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Empty -> {
                        onShowTitleChange(false)
                        showError = false
                    }

                    is AsyncImagePainter.State.Error -> {
                        delay(DELAY.toLong())
                        if (painter.state is AsyncImagePainter.State.Error) showError = true
                        onShowTitleChange(false)
                    }
                }
            }

            when (state) {
                is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = MaterialTheme.colorScheme.background,
                        )
                    }
                }

                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = painter,
                        contentDescription = pokemonName,
                        modifier = modifier.fillMaxSize(),
                        contentScale = ContentScale.FillHeight,
                        alignment = Alignment.Center,
                    )
                }

                is AsyncImagePainter.State.Error -> {
                    if (showError) DrawErrorImage() else Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}