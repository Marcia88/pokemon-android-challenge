package com.example.pokemonapplication.domain.usecases.favovorite

import kotlinx.coroutines.flow.Flow

interface FavoritesUseCase {
    fun getFavoritesFlow(): Flow<Set<Int>>
    suspend fun isFavorite(id: Int): Boolean
    suspend fun toggleFavorite(id: Int, name: String)
}

