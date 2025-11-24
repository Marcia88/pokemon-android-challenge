package com.example.pokemonapplication.presentation

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pokemonapplication.R
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme
import com.example.pokemonapplication.presentation.ui.pokemon_card.PokemonCard
import com.example.pokemonapplication.presentation.ui.pokemon_card.PokemonProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritePokemonsScreen(
    navController: NavHostController,
    viewModel: PokemonListViewModel
) {
    val activity = LocalContext.current as? Activity

    val favoritesIdsState by viewModel.favoritesFlow.collectAsState(initial = emptySet())

    val state = viewModel.state.collectAsState().value

    val favoriteDetails: List<PokemonDetailModel> =
        state.details.values.filter { favoritesIdsState.contains(it.id) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.favorites)) },
            navigationIcon = {
                IconButton(onClick = {
                    if (!navController.navigateUp()) {
                        activity?.finish()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        if (favoriteDetails.isEmpty()) {
            DrawEmptyView()
        } else {
            DrawFavoriteList(favoriteDetails = favoriteDetails, onFavoriteSelected = { pokemon ->
                navController.navigate("detail/${pokemon.name}")
            })
        }
    }
}

@Composable
fun DrawFavoriteList(
    favoriteDetails: List<PokemonDetailModel>,
    onFavoriteSelected: (PokemonDetailModel) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(favoriteDetails) { pokemon ->
            PokemonCard(
                pokemonDetail = pokemon,
                modifier = Modifier.padding(8.dp),
                onClick = { onFavoriteSelected.invoke(pokemon) }
            )
        }
    }
}

@Composable
fun DrawEmptyView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.favorites_empty_message))
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawEmptyViewPreview() {
    DrawEmptyView()
}

@Preview(showBackground = true)
@Composable
private fun DrawFavoriteListPreview() {
    val provider = PokemonProvider()
    val items = provider.values.toList()

    PokemonApplicationTheme {
        DrawFavoriteList(
            favoriteDetails = items,
            onFavoriteSelected = {}
        )
    }
}