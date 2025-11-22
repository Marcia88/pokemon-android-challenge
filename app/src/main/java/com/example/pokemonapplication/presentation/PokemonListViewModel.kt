package com.example.pokemonapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapplication.domain.model.PokemonListModel
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.domain.usecases.GetPokemonList
import com.example.pokemonapplication.domain.usecases.GetPokemonDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PokemonListIntent {
    object Load : PokemonListIntent
}

data class PokemonListState(
    val isLoading: Boolean = false,
    val data: PokemonListModel? = null,
    val error: String? = null,
    val details: Map<String, PokemonDetailModel> = emptyMap(),
    val detailsLoading: Set<String> = emptySet()
)

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonList: GetPokemonList,
    private val getPokemonDetail: GetPokemonDetail
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonListState())
    val state: StateFlow<PokemonListState> = _state

    fun process(intent: PokemonListIntent) {
        when (intent) {
            is PokemonListIntent.Load -> getPokemonList()
        }
    }

    private fun getPokemonList() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            getPokemonList.getPokemonList().collect { result ->
                result.fold(
                    onSuccess = { data ->
                        _state.value = PokemonListState(isLoading = false, data = data)
                        preGetPokemonDetails(data.results.map { it.name }, parallelism = 4, limit = 10)
                    }, onFailure = { throwable ->
                        _state.value = PokemonListState(
                            isLoading = false,
                            error = throwable.message ?: "Unknown error"
                        )
                    })
            }
        }
    }

    fun getCachedPokemonDetail(name: String): PokemonDetailModel? = _state.value.details[name]

    private suspend fun getPokemonDetailSuspend(name: String) {
        if (_state.value.details.containsKey(name) || _state.value.detailsLoading.contains(name)) return
        _state.value = _state.value.copy(detailsLoading = _state.value.detailsLoading + name)

        getPokemonDetail.getPokemonDetail(name).collect { result ->
            result.fold(
                onSuccess = { detail ->
                    _state.value = _state.value.copy(
                        details = _state.value.details + (name to detail),
                        detailsLoading = _state.value.detailsLoading - name
                    )
                }, onFailure = {
                    _state.value = _state.value.copy(
                        detailsLoading = _state.value.detailsLoading - name
                    )
                }
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun preGetPokemonDetails(names: List<String>, parallelism: Int = 4, limit: Int = 10) {
        viewModelScope.launch {
            names.asFlow()
                .take(limit)
                .flatMapMerge(concurrency = parallelism) { name ->
                    flowOf(name)
                }
                .collect { name ->
                    if (_state.value.details.containsKey(name)) return@collect
                    getPokemonDetailSuspend(name)
                }
        }
    }
}
