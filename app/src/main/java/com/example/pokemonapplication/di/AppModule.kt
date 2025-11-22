package com.example.pokemonapplication.di

import com.example.pokemonapplication.data.datasources.PokemonDetailService
import com.example.pokemonapplication.data.datasources.PokemonDetailServiceImpl
import com.example.pokemonapplication.data.datasources.PokemonListService
import com.example.pokemonapplication.data.datasources.PokemonListServiceImpl
import com.example.pokemonapplication.data.mapper.PokemonDetailMapper
import com.example.pokemonapplication.data.mapper.PokemonListMapper
import com.example.pokemonapplication.data.repositories.detail.PokemonDetailRemoteRepository
import com.example.pokemonapplication.data.repositories.detail.PokemonDetailRepository
import com.example.pokemonapplication.data.repositories.pokemonlist.PokemonListRemoteRepository
import com.example.pokemonapplication.data.repositories.pokemonlist.PokemonListRepository
import com.example.pokemonapplication.domain.usecases.GetPokemonList
import com.example.pokemonapplication.domain.usecases.GetPokemonListImpl
import com.example.pokemonapplication.domain.usecases.GetPokemonDetail
import com.example.pokemonapplication.domain.usecases.GetPokemonDetailImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
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
    }
}
