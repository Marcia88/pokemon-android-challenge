package com.example.pokemonapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokemonapplication.data.local.entity.CachedSearchEntity
import com.example.pokemonapplication.data.local.entity.SavedSearchEntity

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedSearch(saved: SavedSearchEntity)

    @Query("SELECT * FROM saved_searches ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentSavedSearches(limit: Int): List<SavedSearchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedSearch(cached: CachedSearchEntity)

    @Query("SELECT * FROM cached_searches WHERE query = :query LIMIT 1")
    suspend fun getCachedSearch(query: String): CachedSearchEntity?
}


