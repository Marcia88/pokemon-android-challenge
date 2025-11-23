package com.example.pokemonapplication.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_searches")
data class CachedSearchEntity(
    @PrimaryKey val query: String,
    @ColumnInfo(name = "response_json") val responseJson: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)

