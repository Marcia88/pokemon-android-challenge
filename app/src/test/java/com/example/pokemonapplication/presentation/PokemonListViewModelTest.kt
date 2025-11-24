package com.example.pokemonapplication.presentation

import com.example.pokemonapplication.domain.model.PokemonListModel
import com.example.pokemonapplication.domain.model.PokemonModel
import com.example.pokemonapplication.domain.usecases.favovorite.FavoritesUseCase
import com.example.pokemonapplication.domain.usecases.pokemon_detail.GetPokemonDetail
import com.example.pokemonapplication.domain.usecases.pokemon_list.GetPokemonList
import com.example.pokemonapplication.domain.usecases.search.SearchPokemonUseCase
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {

    private val getPokemonList = mockk<GetPokemonList>()
    private val getPokemonDetail = mockk<GetPokemonDetail>()
    private val searchUseCase = mockk<SearchPokemonUseCase>()
    private val favoritesUseCase = mockk<FavoritesUseCase>()

    @AfterTest
    fun tearDown() {
        try {
            Dispatchers.resetMain()
        } catch (_: Throwable) {
        }
        clearAllMocks()
    }

    @Test
    fun `loadFirstPage emits success and requests details`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val model = PokemonListModel(
            count = 1,
            next = null,
            previous = null,
            results = listOf(PokemonModel("pokemon-a", "url-a"))
        )

        every { getPokemonList.getPokemonList(20, 0) } returns flowOf(Result.success(model))
        every { getPokemonDetail.getPokemonDetail(any()) } returns flowOf(Result.failure(RuntimeException("no detail")))
        every { favoritesUseCase.getFavoritesFlow() } returns flowOf<Set<Int>>(emptySet())

        val vm = PokemonListViewModel(getPokemonList, getPokemonDetail, searchUseCase, favoritesUseCase)
        vm.process(PokemonListIntent.Load)

        testScheduler.advanceUntilIdle()

        val state = vm.state.value
        assertNotNull(state.data)
        assertEquals(1, state.data.count)
        assertTrue(state.isLoading.not())

        verify { getPokemonList.getPokemonList(20, 0) }
        verify { getPokemonDetail.getPokemonDetail("pokemon-a") }
    }

    @Test
    fun `loadFirstPage emits failure and sets topMessage`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val err = RuntimeException("boom")
        every { getPokemonList.getPokemonList(20, 0) } returns flowOf(Result.failure<PokemonListModel>(err))
        every { favoritesUseCase.getFavoritesFlow() } returns flowOf<Set<Int>>(emptySet())

        val vm = PokemonListViewModel(getPokemonList, getPokemonDetail, searchUseCase, favoritesUseCase)
        vm.process(PokemonListIntent.Load)

        testScheduler.advanceUntilIdle()

        val state = vm.state.value
        assertTrue(state.isLoading.not())
        assertTrue(state.error != null || state.topMessage != null)

        verify { getPokemonList.getPokemonList(20, 0) }
    }
}
