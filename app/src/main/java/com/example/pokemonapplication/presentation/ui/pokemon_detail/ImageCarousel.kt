package com.example.pokemonapplication.presentation.ui.pokemon_detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.domain.model.getHighResImages
import com.example.pokemonapplication.presentation.ui.pokemon_card.PokemonProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    modifier: Modifier = Modifier,
    autoScroll: Boolean = true,
    autoScrollDelayMs: Long = 3000L,
    pokemonDetail: PokemonDetailModel?
) {
    val images = remember(pokemonDetail) {
        pokemonDetail?.getHighResImages().orEmpty()
    }
    if (images.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { images.size }, initialPage = 0)
    val currentIndex by remember { derivedStateOf { pagerState.currentPage } }

    ImageCarouselContent(
        images = images,
        pagerState = pagerState,
        currentIndex = currentIndex,
        modifier = modifier,
        pokemonDetail = pokemonDetail
    )

    if (autoScroll && images.size > 1) {
        LaunchedEffect(images, autoScroll, autoScrollDelayMs) {
            while (true) {
                delay(autoScrollDelayMs)
                snapshotFlow { pagerState.isScrollInProgress }
                    .filter { scrolling -> !scrolling }
                    .first()

                val next = (pagerState.currentPage + 1) % images.size
                pagerState.animateScrollToPage(next)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarouselContent(
    images: List<String>,
    pagerState: PagerState,
    currentIndex: Int,
    modifier: Modifier = Modifier,
    onImageTap: ((String) -> Unit)? = null,
    pokemonDetail: PokemonDetailModel?
) {
    Box(modifier = modifier) {
        HorizontalPager(
            pagerState,
            modifier = Modifier.fillMaxSize(),
            pageSpacing = 8.dp
        ) { page ->
            val url = images[page]
            HorizontalPagerItem(onImageTap, url, pokemonDetail)
        }

        val scope = rememberCoroutineScope()
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        ) {
            images.forEachIndexed { index, _ ->
                val selected = index == currentIndex
                val size = if (selected) 10.dp else 6.dp
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(size)
                        .clickable { scope.launch { pagerState.animateScrollToPage(index) } }
                        .background(
                            color = if (selected) {
                                pokemonDetail?.types?.firstOrNull()?.type?.backgroundColor()
                                    ?: MaterialTheme.colorScheme.primary
                            } else {
                                Color.Gray
                            },
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageCarouselPreview(@PreviewParameter(PokemonProvider::class) pokemonDetail: PokemonDetailModel?) {
    ImageCarousel(
        pokemonDetail = pokemonDetail,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        autoScrollDelayMs = 2000L,
    )
}

@Preview(showBackground = true)
@Composable
fun ImageCarouselContentPreview(@PreviewParameter(PokemonProvider::class) pokemonDetail: PokemonDetailModel?) {
    val sampleImages = listOf(
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png"
    )

    val pagerState = rememberPagerState(pageCount = { sampleImages.size }, initialPage = 0)

    ImageCarouselContent(
        images = sampleImages,
        pagerState = pagerState,
        currentIndex = 0,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        onImageTap = {},
        pokemonDetail = pokemonDetail
    )
}