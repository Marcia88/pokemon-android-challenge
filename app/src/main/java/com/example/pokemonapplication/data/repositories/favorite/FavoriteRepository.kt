package com.example.pokemonapplication.data.repositories.favorite

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavoritesFlow(): Flow<Set<Int>>
    suspend fun addFavorite(id: Int, name: String)
    suspend fun removeFavorite(id: Int)
    suspend fun exists(id: Int): Boolean
}