package com.example.pokemonapplication.presentation.ui.pokemonCard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.ImageResult
import kotlinx.coroutines.delay
import com.example.pokemonapplication.R
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme

const val PLACEHOLDER = "..."

@Composable
fun PokemonCard(pokemonDetail: PokemonDetailModel?, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.size(150.dp),
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
            verticalArrangement = Arrangement.Top
        ) {
            Box {
                this@Column.AnimatedVisibility(
                    visible = (pokemonDetail?.name != null),
                    enter = fadeIn(tween(220)) + scaleIn(tween(220))
                ) {
                    Text(
                        text = pokemonDetail?.name ?: PLACEHOLDER,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                val imageUrl = pokemonDetail?.imageUrl

                if (!imageUrl.isNullOrEmpty()) {
                    var timedOut by remember { mutableStateOf(false) }
                    var hasError by remember { mutableStateOf(false) }

                    LaunchedEffect(imageUrl) {
                        timedOut = false
                        hasError = false
                    }

                    val painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .listener(onStart = { _: ImageRequest -> hasError = false }, onError = { _: ImageRequest, _: ImageResult -> hasError = true })
                            .build()
                    )

                    val state = painter.state

                    LaunchedEffect(state) {
                        if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Empty) {
                            timedOut = false
                            var elapsed = 0L
                            val interval = 200L
                            val timeout = 3000L
                            while ((painter.state is AsyncImagePainter.State.Loading || painter.state is AsyncImagePainter.State.Empty) && elapsed < timeout) {
                                delay(interval)
                                elapsed += interval
                            }
                            if (painter.state is AsyncImagePainter.State.Loading || painter.state is AsyncImagePainter.State.Empty) {
                                timedOut = true
                            }
                        } else {
                            timedOut = false
                        }
                    }

                    this@Column.AnimatedVisibility(
                        visible = state is AsyncImagePainter.State.Success,
                        enter = fadeIn(tween(300)) + scaleIn(tween(300))
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = pokemonDetail.name,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(100.dp),
                        )
                    }

                    if ((state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Empty) && !timedOut && !hasError) {
                        CircularProgressIndicator(modifier = Modifier.size(36.dp))
                    } else if (state is AsyncImagePainter.State.Error || timedOut || hasError) {
                        DrawErrorImage()
                    }
                } else {
                    DrawErrorImage()
                }
            }
        }
    }
}

@Composable
fun DrawErrorImage() {
    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Error,
                contentDescription = "Image load error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = stringResource(R.string.image_load_failed),
                style = MaterialTheme.typography.labelSmall
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

@Preview
@Composable
fun PlaceholderPokemonCardPreview() {
    PokemonApplicationTheme {
        PokemonCard(null)
    }
}
