package com.example.pokemonapplication.ui.pokemonlist

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.pokemonapplication.R
import com.example.pokemonapplication.domain.model.PokemonModel
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.presentation.PokemonListViewModel
import com.example.pokemonapplication.presentation.PokemonListIntent
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme
import com.example.pokemonapplication.ui.PokemonCard
import com.example.pokemonapplication.ui.PokemonProvider

@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.process(PokemonListIntent.Load)
    }

    Crossfade(targetState = state) { s ->
        when {
            s.isLoading || s.data == null -> {
                LoadingView()
            }

            s.error != null -> {
                ErrorView(s.error, onRetry = { viewModel.process(PokemonListIntent.Load) })
            }

            else -> {
                val data = s.data
                Column(modifier = modifier.fillMaxSize()) {
                    DrawPokemonCarrousel(
                        data.results,
                        getCached = viewModel::getCachedPokemonDetail
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    val loadingText = stringResource(id = R.string.loading)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.32f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = loadingText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ErrorView(error: String?, onRetry: (() -> Unit)? = null) {
    val unknown = stringResource(id = R.string.error_unknown)

    val displayed = if (error.isNullOrBlank()) {
        unknown
    } else {
        stringResource(id = R.string.error_message, error)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .semantics { contentDescription = displayed },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.error_title),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = displayed,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        onRetry?.let {
            Button(onClick = it) {
                Text(text = stringResource(id = R.string.retry))
            }
        }
    }
}

@Composable
fun DrawPokemonCarrousel(
    results: List<PokemonModel>,
    getCached: (String) -> PokemonDetailModel?,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(24.dp)
    ) {
        items(results) { pokemon ->
            getCached(pokemon.name)?.let { PokemonCard(it) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonCarrouselPreview(@PreviewParameter(PokemonListProvider::class) pokemonList: List<PokemonModel>) {
    PokemonApplicationTheme {
        val provider = PokemonProvider()
        val detailMap = provider.values.toList().associateBy { it.name }
        DrawPokemonCarrousel(pokemonList, { name ->
            detailMap[name] ?: provider.values.firstOrNull()
        })
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonErrorPreview() {
    PokemonApplicationTheme {
        ErrorView("Error")
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonLoadingPreview() {
    PokemonApplicationTheme {
        LoadingView()
    }
}
