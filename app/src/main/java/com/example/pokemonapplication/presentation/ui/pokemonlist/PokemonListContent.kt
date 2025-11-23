package com.example.pokemonapplication.presentation.ui.pokemonlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.domain.model.PokemonModel
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme
import com.example.pokemonapplication.presentation.ui.search.SearchBar
import com.example.pokemonapplication.presentation.ui.pokemonCard.PokemonCard
import com.example.pokemonapplication.presentation.ui.pokemonCard.PokemonProvider
import kotlinx.coroutines.flow.collectLatest

const val SKELETON_ITEMS = 20

@Composable
fun PokemonListContent(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (String) -> Unit,
    onSearch: ((String) -> Unit)? = null,
    results: List<PokemonModel>,
    hasNext: Boolean,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    getPokemonDetail: (String) -> PokemonDetailModel?
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                SearchBar(
                    value = query,
                    onValueChange = { onQueryChange(it) },
                    onSearch = onSearch
                )
            }
        }
    ) { innerPadding ->
       Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            DrawPokemonGrid(
                results = results,
                hasNext = hasNext,
                isLoadingMore = isLoadingMore,
                getPokemonDetail = getPokemonDetail,
                onLoadMore = onLoadMore,
                modifier = Modifier.fillMaxSize()
            )

            if (isLoadingMore) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun DrawPokemonGrid(
    results: List<PokemonModel>,
    hasNext: Boolean,
    isLoadingMore: Boolean,
    getPokemonDetail: (String) -> PokemonDetailModel?,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()

    // Last item -> load next page
    LaunchedEffect(gridState, hasNext, isLoadingMore, results.size) {
        snapshotFlow {
            val info = gridState.layoutInfo
            (info.visibleItemsInfo.lastOrNull()?.index ?: -1) >= (info.totalItemsCount - 1)
        }.collectLatest { atEnd ->
            val total = gridState.layoutInfo.totalItemsCount
            if (shouldLoadMore(hasNext, isLoadingMore, total, atEnd)) {
                onLoadMore()
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(start = 24.dp, top = 0.dp, end = 24.dp, bottom = 0.dp)
    ) {
        if (results.isEmpty()) {
            // Show skeleton
            items(count = SKELETON_ITEMS, key = { it }) { index ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(300 + index * 40))
                ) {
                    PokemonCard(null)
                }
            }
        } else {
            items(results, key = { it.name }) { pokemon ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(220))
                ) {
                    PokemonCard(getPokemonDetail(pokemon.name))
                }
            }
        }
    }
}

private fun shouldLoadMore(hasNext: Boolean, isLoadingMore: Boolean, total: Int, atEnd: Boolean): Boolean {
    return hasNext && !isLoadingMore && total > 0 && atEnd
}

@Preview(showBackground = true)
@Composable
fun PokemonListContentPreview(@PreviewParameter(PokemonListProvider::class) pokemonList: List<PokemonModel>) {
    PokemonApplicationTheme {
        val provider = PokemonProvider()
        val detailMap = provider.values.toList().associateBy { it.name }

        PokemonListContent(
            modifier = Modifier,
            query = "",
            onQueryChange = {},
            onSearch = {},
            results = pokemonList,
            hasNext = true,
            isLoadingMore = false,
            onLoadMore = {},
            getPokemonDetail = { name -> detailMap[name] ?: provider.values.firstOrNull() }
        )
    }
}
