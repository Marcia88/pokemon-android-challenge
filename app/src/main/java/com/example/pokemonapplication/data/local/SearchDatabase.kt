package com.example.pokemonapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokemonapplication.data.local.dao.SearchDao
import com.example.pokemonapplication.data.local.entity.CachedSearchEntity
import com.example.pokemonapplication.data.local.entity.SavedSearchEntity

@Database(
    entities = [SavedSearchEntity::class, CachedSearchEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao
}

