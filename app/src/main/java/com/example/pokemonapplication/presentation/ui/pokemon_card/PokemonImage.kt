package com.example.pokemonapplication.presentation.ui.pokemon_card

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
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
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            contentDescription = pokemonName,
            modifier = modifier
        ) {
            val painterInstance = painter
            val state = painterInstance.state
            var showError by remember { mutableStateOf(false) }
            var visible by remember { mutableStateOf(false) }
            val hasAnimatedBefore = rememberSaveable(pokemonName) { mutableStateOf(false) }

            LaunchedEffect(state) {
                when (state) {
                    is AsyncImagePainter.State.Success -> {
                        onShowTitleChange(true)
                        showError = false
                        visible = true
                        if (!hasAnimatedBefore.value) {
                            hasAnimatedBefore.value = true
                        }
                    }

                    is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Empty -> {
                        visible = false
                        onShowTitleChange(false)
                        showError = false
                    }

                    is AsyncImagePainter.State.Error -> {
                        visible = false
                        delay(DELAY.toLong())
                        if (painterInstance.state is AsyncImagePainter.State.Error) showError = true
                        onShowTitleChange(false)
                    }
                }
            }

            val scale by animateFloatAsState(
                targetValue = if (visible) 1f else 0.8f,
                animationSpec = if (!hasAnimatedBefore.value) tween(300) else tween(0),
                label = ""
            )

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
                        painter = painterInstance,
                        contentDescription = pokemonName,
                        modifier = Modifier
                            .size(140.dp)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            },
                        contentScale = ContentScale.Inside,
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