package com.example.pokemonapplication.domain.usecases.favovorite

import com.example.pokemonapplication.data.repositories.favorite.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesUseCaseImpl @Inject constructor(
    private val repo: FavoriteRepository
) : FavoritesUseCase {

    override fun getFavoritesFlow(): Flow<Set<Int>> = repo.getFavoritesFlow()

    override suspend fun isFavorite(id: Int): Boolean = repo.exists(id)

    override suspend fun toggleFavorite(id: Int, name: String) {
        if (repo.exists(id)) repo.removeFavorite(id) else repo.addFavorite(id, name)
    }
}