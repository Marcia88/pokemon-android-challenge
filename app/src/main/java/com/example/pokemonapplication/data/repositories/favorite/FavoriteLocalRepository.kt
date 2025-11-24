package com.example.pokemonapplication.data.repositories.favorite

import com.example.pokemonapplication.data.local.dao.FavoriteDao
import com.example.pokemonapplication.data.local.entity.favourite.FavoriteEntity
import com.example.pokemonapplication.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteLocalRepository @Inject constructor(
    private val dao: FavoriteDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FavoriteRepository {

    override fun getFavoritesFlow(): Flow<Set<Int>> =
        dao.getAllFavorites()
            .map { list -> list.map { it.id }.toSet() }
            .flowOn(ioDispatcher)

    override suspend fun addFavorite(id: Int, name: String) {
        withContext(ioDispatcher) {
            dao.insertFavorite(FavoriteEntity(id = id, name = name))
        }
    }

    override suspend fun removeFavorite(id: Int) {
        withContext(ioDispatcher) {
            dao.deleteFavoriteById(id)
        }
    }

    override suspend fun exists(id: Int): Boolean =
        withContext(ioDispatcher) {
            dao.exists(id)
        }
}