package com.example.pokemonapplication.presentation.ui.pokemon_card

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import com.example.pokemonapplication.domain.model.*

class PokemonCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun fakeDetail(name: String): PokemonDetailModel {
        return PokemonDetailModel(
            id = 1,
            name = name,
            imageUrl = null,
            types = emptyList(),
            abilities = emptyList(),
            moves = emptyList(),
            stats = emptyMap(),
            spritesModel = SpritesModel(
                other = OtherSpritesModel(
                    dreamWorldModel = DreamWorldModel(
                        frontDefault = "https://example.com/img.svg"
                    )
                )
            ),
            species = "",
            height = 10,
            weight = 20
        )
    }

    @Test
    fun when_detail_is_null_shows_placeholder() {
        composeTestRule.setContent {
            PokemonCard(pokemonDetail = null)
        }

        composeTestRule
            .onNodeWithText("...", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun title_shows_name_when_visible() {
        val model = fakeDetail("pikachu")

        composeTestRule.setContent {
            PokemonTitle(
                pokemonDetail = model,
                imageUrl = model.spritesModel?.other?.dreamWorldModel?.frontDefault,
                showTitle = true,
                hasAnimated = false,
                onAnimatedDone = {},
            )
        }

        composeTestRule
            .onNodeWithText("pikachu", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun title_is_hidden_when_showTitle_is_false() {
        val model = fakeDetail("pikachu")

        composeTestRule.setContent {
            PokemonTitle(
                pokemonDetail = model,
                imageUrl = model.spritesModel?.other?.dreamWorldModel?.frontDefault,
                showTitle = false,
                hasAnimated = false,
                onAnimatedDone = {},
            )
        }

        composeTestRule
            .onNodeWithText("pikachu", useUnmergedTree = true)
            .assertDoesNotExist()
    }
}
