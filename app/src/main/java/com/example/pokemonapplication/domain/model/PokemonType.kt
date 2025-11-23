package com.example.pokemonapplication.domain.model

import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.pokemonapplication.R

enum class PokemonType(@ColorRes val backgroundRes: Int) {
    NORMAL(R.color.pokemon_type_normal),
    FIRE(R.color.pokemon_type_fire),
    WATER(R.color.pokemon_type_water),
    ELECTRIC(R.color.pokemon_type_electric),
    GRASS(R.color.pokemon_type_grass),
    ICE(R.color.pokemon_type_ice),
    FIGHTING(R.color.pokemon_type_fighting),
    POISON(R.color.pokemon_type_poison),
    GROUND(R.color.pokemon_type_ground),
    FLYING(R.color.pokemon_type_flying),
    PSYCHIC(R.color.pokemon_type_psychic),
    BUG(R.color.pokemon_type_bug),
    ROCK(R.color.pokemon_type_rock),
    GHOST(R.color.pokemon_type_ghost),
    DRAGON(R.color.pokemon_type_dragon),
    DARK(R.color.pokemon_type_dark),
    STEEL(R.color.pokemon_type_steel),
    FAIRY(R.color.pokemon_type_fairy);

    companion object {
        fun fromString(type: String): PokemonType? {
            return values().firstOrNull { it.name.equals(type, ignoreCase = true) }
        }
    }

    @Composable
    fun backgroundColor(): Color = colorResource(id = backgroundRes)
}
