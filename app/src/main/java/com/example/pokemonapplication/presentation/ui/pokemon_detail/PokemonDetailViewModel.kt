package com.example.pokemonapplication.presentation.ui.pokemon_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapplication.domain.usecases.favovorite.FavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val favoritesUseCase: FavoritesUseCase
): ViewModel() {

    fun isFavoriteFlow(id: Int): Flow<Boolean> = favoritesUseCase.getFavoritesFlow().map { it.contains(id) }

    fun toggleFavorite(id: Int, name: String) {
        viewModelScope.launch {
            favoritesUseCase.toggleFavorite(id, name)
        }
    }
}
