package com.example.pokemonapplication.domain.usecases.pokemon_list

import com.example.pokemonapplication.data.repositories.pokemonlist.PokemonListRepository
import com.example.pokemonapplication.domain.model.PokemonListModel
import com.example.pokemonapplication.domain.model.PokemonModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class GetPokemonListImplTest {

    private val repository = mockk<PokemonListRepository>()
    private lateinit var usecase: GetPokemonListImpl

    @Test
    fun `getPokemonList emits success when repository returns success`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        usecase = GetPokemonListImpl(repository, dispatcher)

        val model = PokemonListModel(
            count = 1,
            next = null,
            previous = null,
            results = listOf(PokemonModel("a", "u"))
        )

        coEvery { repository.getPokemonList(20, 0) } returns Result.success(model)
        testScheduler.advanceUntilIdle()

        val emitted = usecase.getPokemonList(20, 0).first()
        assertTrue(emitted.isSuccess)
        assertEquals(1, emitted.getOrNull()?.count)

        coVerify(exactly = 1) { repository.getPokemonList(20, 0) }
    }

    @Test
    fun `getPokemonList emits failure when repository returns failure`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        usecase = GetPokemonListImpl(repository, dispatcher)

        val err = RuntimeException("boom")
        coEvery { repository.getPokemonList(20, 0) } returns Result.failure(err)
        testScheduler.advanceUntilIdle()

        val emitted = usecase.getPokemonList(20, 0).first()
        assertTrue(emitted.isFailure)
        assertEquals(err, emitted.exceptionOrNull())

        coVerify(exactly = 1) { repository.getPokemonList(20, 0) }
    }
}
