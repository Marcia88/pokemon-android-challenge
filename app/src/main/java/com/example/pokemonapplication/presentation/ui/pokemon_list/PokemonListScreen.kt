package com.example.pokemonapplication.presentation.ui.pokemon_list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokemonapplication.R
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.presentation.PokemonListIntent
import com.example.pokemonapplication.presentation.PokemonListViewModel
import com.example.pokemonapplication.presentation.theme.PokemonApplicationTheme

@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel,
    onItemClick: ((PokemonDetailModel) -> Unit)? = null
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.process(PokemonListIntent.Load)
    }

    state.topMessage?.let { topMsg ->
        DrawPopup(topMsg, onClicked = { viewModel.process(PokemonListIntent.ClearTopMessage) })
    }

    val screenMode = when {
        state.error != null -> ScreenMode.Error
        else -> ScreenMode.Content
    }

    Crossfade(targetState = screenMode) { mode ->
        when (mode) {
            ScreenMode.Error -> {
                ErrorView(
                    modifier,
                    state.error,
                    onRetry = { viewModel.process(PokemonListIntent.Load) })
            }
            ScreenMode.Content -> {
                val data = state.data
                PokemonListContent(
                    modifier = modifier,
                    query = state.query,
                    onQueryChange = { viewModel.process(PokemonListIntent.UpdateQuery(it)) },
                    onSearch = { queryText -> viewModel.process(PokemonListIntent.Search(queryText)) },
                    results = data?.results ?: emptyList(),
                    hasNext = data?.next != null,
                    isLoadingMore = state.isLoadingMore,
                    onLoadMore = { viewModel.process(PokemonListIntent.LoadMore) },
                    getPokemonDetail = viewModel::getPokemonDetail,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
fun DrawPopup(topMsg: String, onClicked: (() -> Unit)? = null) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = topMsg,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { onClicked?.invoke() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrawPopupPreview() {
    PokemonApplicationTheme {
        DrawPopup(topMsg = "Server error (500). Please try again later.")
    }
}

private enum class ScreenMode { Error, Content }

@Composable
fun ErrorView(modifier: Modifier = Modifier, error: String?, onRetry: (() -> Unit)? = null) {
    val unknown = stringResource(id = R.string.error_unknown)

    val displayed = if (error.isNullOrBlank()) {
        unknown
    } else {
        stringResource(id = R.string.error_message, error)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .semantics { contentDescription = displayed },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.error_title),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = displayed,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        onRetry?.let {
            Button(onClick = it) {
                Text(text = stringResource(id = R.string.retry))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonErrorPreview() {
    PokemonApplicationTheme {
        ErrorView(error = "Error")
    }
}
