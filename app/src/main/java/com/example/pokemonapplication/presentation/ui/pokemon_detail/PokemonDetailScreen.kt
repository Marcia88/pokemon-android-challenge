package com.example.pokemonapplication.presentation.ui.pokemon_detail

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pokemonapplication.R
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.presentation.ui.pokemon_card.PokemonProvider
import com.example.pokemonapplication.presentation.ui.pokemon_detail.tabs.PokemonDetailTabs
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonDetail: PokemonDetailModel?,
    navController: NavHostController,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as? Activity
    val title = stringResource(id = R.string.pokemon_details_title)

    BackHandler {
        if (!navController.navigateUp()) {
            activity?.finish()
        }
    }

    var isFavorite by rememberSaveable { mutableStateOf(false) }
    val inspection = LocalInspectionMode.current
    val scope = rememberCoroutineScope()

    if (inspection && pokemonDetail != null) {
        isFavorite = false
    } else {
        LaunchedEffect(pokemonDetail) {
            pokemonDetail?.let { p ->
                viewModel.isFavoriteFlow(p.id).collect { fav ->
                    isFavorite = fav
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            title = { Text(text = title) },
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
            },
            actions = {
                FavoriteToggle(
                    isFavorite = isFavorite,
                    onToggle = {
                        pokemonDetail?.let { p ->
                            scope.launch { viewModel.toggleFavorite(p.id, p.name) }
                        }
                    },
                    color = pokemonDetail?.types?.firstOrNull()?.type?.backgroundColor() ?:
                    colorResource(R.color.pokemon_type_fighting)

                )

                ShareButton(pokemonDetail = pokemonDetail)
            }
        )

        ImageCarousel(
            pokemonDetail = pokemonDetail,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )

        pokemonDetail?.let { PokemonDetailTabs(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteToggle(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    color: Color
) {
    val favText = if (isFavorite) {
        stringResource(id = R.string.remove_from_favorites)
    } else {
        stringResource(id = R.string.add_to_favorites)
    }
    val tooltipState = rememberTooltipState()

    TooltipBox(
        state = tooltipState,
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                Text(text = favText)
            }
        }
    ) {
        IconButton(onClick = onToggle) {
            val favIcon = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
            Icon(imageVector = favIcon, contentDescription = favText, tint = color)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareButton(
    pokemonDetail: PokemonDetailModel?
) {
    val context = LocalContext.current
    val shareTooltipText = stringResource(id = R.string.share_action)
    val shareTooltipState = rememberTooltipState()

    TooltipBox(
        state = shareTooltipState,
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                Text(text = shareTooltipText)
            }
        }
    ) {
        IconButton(onClick = {
            pokemonDetail?.let { p ->
                val pokemonName =
                    p.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                val shareText = context.getString(
                    R.string.share_text_template,
                    pokemonName,
                    pokemonDetail.imageUrl
                )
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                val chooser =
                    Intent.createChooser(sendIntent, context.getString(R.string.share_action))
                context.startActivity(chooser)
            }
        }) {
            Icon(imageVector = Icons.Filled.Share, contentDescription = shareTooltipText)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonDetailScreenPreview(@PreviewParameter(PokemonProvider::class) pokemonDetail: PokemonDetailModel) {
    val navController = rememberNavController()
    PokemonDetailScreen(pokemonDetail = pokemonDetail, navController = navController)
}
