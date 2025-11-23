package com.example.pokemonapplication.presentation.ui.pokemon_card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme
import java.util.concurrent.ConcurrentHashMap

const val PLACEHOLDER = "..."
const val DELAY = 300
private const val ANIMATION_TWEEN_MS = 220

private object AnimatedTitleStore {
    private val set = ConcurrentHashMap.newKeySet<String>()

    fun hasAnimated(name: String?): Boolean {
        if (name.isNullOrEmpty()) return false
        return set.contains(name)
    }

    fun markAnimated(name: String?) {
        if (name.isNullOrEmpty()) return
        set.add(name)
    }
}

@Composable
fun PokemonCard(
    pokemonDetail: PokemonDetailModel?,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val imageUrl = pokemonDetail?.imageUrl
    var showTitle by remember(imageUrl) { mutableStateOf(false) }

    val pokemonName = pokemonDetail?.name
    var hasAnimated by remember(pokemonName) { mutableStateOf(AnimatedTitleStore.hasAnimated(pokemonName)) }

    Card(
        modifier = modifier
            .size(150.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onBackground)
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            PokemonTitle(
                pokemonDetail = pokemonDetail,
                imageUrl = imageUrl,
                showTitle = showTitle,
                hasAnimated = hasAnimated,
                onAnimatedDone = {
                    hasAnimated = true
                    AnimatedTitleStore.markAnimated(pokemonName)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                PokemonImage(
                    imageUrl = imageUrl,
                    pokemonName = pokemonDetail?.name,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    onShowTitleChange = { showTitle = it }
                )
            }
        }
    }
}

@Composable
fun PokemonTitle(
    pokemonDetail: PokemonDetailModel?,
    imageUrl: String?,
    showTitle: Boolean,
    hasAnimated: Boolean,
    onAnimatedDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val titleText = pokemonDetail?.name.orEmpty()

    Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
        val titleVisible = titleText.isNotEmpty() && showTitle && !imageUrl.isNullOrEmpty()

        LaunchedEffect(titleVisible, hasAnimated) {
            if (titleVisible && !hasAnimated) {
                onAnimatedDone()
            }
        }

        AnimatedVisibility(
            visible = titleVisible,
            enter = if (!hasAnimated) {
                fadeIn(tween(ANIMATION_TWEEN_MS)) +
                    scaleIn(tween(ANIMATION_TWEEN_MS))
            } else {
                EnterTransition.None
            }
        ) {
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.background
            )
        }

        if (!titleVisible) {
            Text(
                text = PLACEHOLDER, style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.background
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
