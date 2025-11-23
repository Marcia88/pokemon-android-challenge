package com.example.pokemonapplication.di

import android.content.Context
import androidx.room.Room
import com.example.pokemonapplication.data.datasources.PokemonDetailService
import com.example.pokemonapplication.data.datasources.PokemonDetailServiceImpl
import com.example.pokemonapplication.data.datasources.PokemonListService
import com.example.pokemonapplication.data.datasources.PokemonListServiceImpl
import com.example.pokemonapplication.data.local.SearchDatabase
import com.example.pokemonapplication.data.local.dao.SearchDao
import com.example.pokemonapplication.data.mapper.PokemonDetailMapper
import com.example.pokemonapplication.data.mapper.PokemonListMapper
import com.example.pokemonapplication.data.repositories.detail.PokemonDetailRemoteRepository
import com.example.pokemonapplication.data.repositories.detail.PokemonDetailRepository
import com.example.pokemonapplication.data.repositories.pokemonlist.PokemonListRemoteRepository
import com.example.pokemonapplication.data.repositories.pokemonlist.PokemonListRepository
import com.example.pokemonapplication.data.repositories.search.SearchCacheRepository
import com.example.pokemonapplication.data.repositories.search.SearchCacheRepositoryImpl
import com.example.pokemonapplication.domain.usecases.GetPokemonDetail
import com.example.pokemonapplication.domain.usecases.GetPokemonDetailImpl
import com.example.pokemonapplication.domain.usecases.GetPokemonList
import com.example.pokemonapplication.domain.usecases.GetPokemonListImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindPokemonRepository(
        impl: PokemonListRemoteRepository
    ): PokemonListRepository

    @Binds
    abstract fun bindGetPokemonList(
        impl: GetPokemonListImpl
    ): GetPokemonList

    @Binds
    abstract fun bindPokemonListService(
        impl: PokemonListServiceImpl
    ): PokemonListService

    @Binds
    abstract fun bindPokemonDetailService(
        impl: PokemonDetailServiceImpl
    ): PokemonDetailService

    @Binds
    abstract fun bindPokemonDetailRepository(
        impl: PokemonDetailRemoteRepository
    ): PokemonDetailRepository

    @Binds
    abstract fun bindGetPokemonDetail(
        impl: GetPokemonDetailImpl
    ): GetPokemonDetail

    @Binds
    abstract fun bindSearchCacheRepository(
        impl: SearchCacheRepositoryImpl
    ): SearchCacheRepository

    companion object {
        @Provides
        @Singleton
        fun provideHttpClient(): HttpClient = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        @Provides
        @Singleton
        fun providePokemonListMapper(): PokemonListMapper = PokemonListMapper()

        @Provides
        @Singleton
        fun providePokemonDetailMapper(): PokemonDetailMapper = PokemonDetailMapper()

        @Provides
        @Singleton
        fun provideSearchDatabase(@ApplicationContext context: Context): SearchDatabase {
            return Room.databaseBuilder(context, SearchDatabase::class.java, "search_db").build()
        }

        @Provides
        @Singleton
        fun provideSearchDao(db: SearchDatabase): SearchDao = db.searchDao()
    }
}
