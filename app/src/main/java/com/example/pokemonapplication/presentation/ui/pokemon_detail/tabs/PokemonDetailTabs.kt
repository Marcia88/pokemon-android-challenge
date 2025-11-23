package com.example.pokemonapplication.presentation.ui.pokemon_detail.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.presentation.ui.pokemon_card.PokemonProvider
import com.example.pokemonapplication.R

@Composable
fun PokemonDetailTabs(pokemonDetailModel: PokemonDetailModel) {
    val tabs = listOf(
        stringResource(id = R.string.about_title),
        stringResource(id = R.string.tab_base_stats),
        stringResource(id = R.string.tab_moves)
    )
    val selectedTab = remember { mutableIntStateOf(0) }

    val typeColor = pokemonDetailModel.types.firstOrNull()?.type?.backgroundColor()
        ?: MaterialTheme.colorScheme.onSurface

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = selectedTab.value,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.value]),
                    color = typeColor
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab.value == index,
                    onClick = { selectedTab.value = index },
                    text = { Text(title, color = typeColor) }
                )
            }
        }

        when (selectedTab.value) {
            0 -> PokemonDetailAboutTab(pokemonDetailModel)
            1 -> PokemonDetailBaseStatsTab(pokemonDetailModel)
            2 -> PokemonDetailMovesTab(pokemonDetailModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonDetailTabsPreview(@PreviewParameter(PokemonProvider::class) pokemonDetail: PokemonDetailModel) {
    PokemonDetailTabs(pokemonDetailModel = pokemonDetail)
}
