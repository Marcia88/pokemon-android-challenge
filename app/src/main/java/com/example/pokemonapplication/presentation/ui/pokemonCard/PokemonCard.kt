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
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.pokemonapplication.R
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme
import kotlinx.coroutines.delay

const val PLACEHOLDER = "..."
const val DELAY = 300
private const val ANIMATION_TWEEN_MS = 220

@Composable
fun PokemonCard(pokemonDetail: PokemonDetailModel?, modifier: Modifier = Modifier) {
    val imageUrl = pokemonDetail?.imageUrl
    var showTitle by remember(imageUrl) { mutableStateOf(false) }

    Card(
        modifier = modifier.size(150.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                val titleVisible =
                    (pokemonDetail?.name != null) && showTitle && !imageUrl.isNullOrEmpty()
                if (titleVisible) {
                    this@Column.AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(ANIMATION_TWEEN_MS)) +
                            scaleIn(tween(ANIMATION_TWEEN_MS))
                    ) {
                        Text(
                            text = pokemonDetail.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                } else {
                    Text(text = PLACEHOLDER, style = MaterialTheme.typography.titleLarge)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (!imageUrl.isNullOrEmpty()) {
                    val context = LocalContext.current
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = pokemonDetail.name,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(100.dp)
                    ) {
                        val state = painter.state
                        var showError by remember { mutableStateOf(false) }

                        LaunchedEffect(state) {
                            when (state) {
                                is AsyncImagePainter.State.Success -> {
                                    showTitle = true
                                    showError = false
                                }

                                is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Empty -> {
                                    showTitle = false
                                    showError = false
                                }

                                is AsyncImagePainter.State.Error -> {
                                    delay(DELAY.toLong())
                                    if (painter.state is AsyncImagePainter.State.Error) showError =
                                        true
                                    showTitle = false
                                }
                            }
                        }

                        when (state) {
                            is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Empty -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                                }
                            }

                            is AsyncImagePainter.State.Success -> {
                                Image(
                                    painter = painter,
                                    contentDescription = pokemonDetail.name,
                                    modifier = Modifier.size(100.dp)
                                )
                            }

                            is AsyncImagePainter.State.Error -> {
                                if (showError) DrawErrorImage() else Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) { CircularProgressIndicator(modifier = Modifier.size(32.dp)) }
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    }
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

@Preview
@Composable
fun DrawErrorImagePreview() {
    PokemonApplicationTheme {
        DrawErrorImage()
    }
}
