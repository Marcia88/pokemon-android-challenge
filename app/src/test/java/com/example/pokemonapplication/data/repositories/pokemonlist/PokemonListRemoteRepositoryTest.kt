package com.example.pokemonapplication.data.repositories.pokemonlist

import com.example.pokemonapplication.data.datasources.PokemonListService
import com.example.pokemonapplication.data.mapper.PokemonListMapper
import com.example.pokemonapplication.data.dto.PokemonListResponse
import com.example.pokemonapplication.data.dto.PokemonItemResponse
import com.example.pokemonapplication.domain.model.PokemonListModel
import com.example.pokemonapplication.domain.model.PokemonModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PokemonListRemoteRepositoryTest {

    private val mapper = mockk<PokemonListMapper>()
    private val service = mockk<PokemonListService>()
    private val repository = PokemonListRemoteRepository(service, mapper)

    @Test
    fun `getPokemonList returns mapped model on success`() = runTest {
        val response = PokemonListResponse(
            count = 2,
            next = null,
            previous = null,
            results = listOf(
                PokemonItemResponse("bulbasaur", "url1"),
                PokemonItemResponse("ivysaur", "url2")
            )
        )

        val mapped = PokemonListModel(
            count = 2,
            next = null,
            previous = null,
            results = listOf(
                PokemonModel("bulbasaur", "url1"),
                PokemonModel("ivysaur", "url2")
            )
        )

        coEvery { service.getPokemonList(10, 0) } returns Result.success(response)
        coEvery { mapper.mapToDomain(response) } returns mapped

        val result = repository.getPokemonList(10, 0)

        assertTrue(result.isSuccess)
        val value = result.getOrNull()!!
        assertEquals(2, value.count)
        assertEquals("bulbasaur", value.results[0].name)

        coVerify(exactly = 1) { service.getPokemonList(10, 0) }
        coVerify(exactly = 1) { mapper.mapToDomain(response) }
    }

    @Test
    fun `getPokemonList returns failure when service fails`() = runTest {
        val error = RuntimeException("network")
        coEvery { service.getPokemonList(10, 0) } returns Result.failure(error)

        val result = repository.getPokemonList(10, 0)

        assertTrue(result.isFailure)
        val ex = result.exceptionOrNull()
        assertEquals(error, ex)

        coVerify(exactly = 1) { service.getPokemonList(10, 0) }
    }
}

