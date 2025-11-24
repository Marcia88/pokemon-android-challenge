package com.example.pokemonapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapplication.data.ErrorMapper
import com.example.pokemonapplication.domain.model.PokemonDetailModel
import com.example.pokemonapplication.domain.model.PokemonListModel
import com.example.pokemonapplication.domain.model.PokemonModel
import com.example.pokemonapplication.domain.usecases.pokemon_detail.GetPokemonDetail
import com.example.pokemonapplication.domain.usecases.pokemon_list.GetPokemonList
import com.example.pokemonapplication.domain.usecases.search.SearchPokemonUseCase
import com.example.pokemonapplication.domain.usecases.favovorite.FavoritesUseCase
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
    data class Search(val query: String) : PokemonListIntent
    data class UpdateQuery(val query: String) : PokemonListIntent
    object LoadMore : PokemonListIntent
    object ClearTopMessage : PokemonListIntent
}

data class PokemonListState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val data: PokemonListModel? = null,
    val error: String? = null,
    val details: Map<String, PokemonDetailModel> = emptyMap(),
    val detailsLoading: Set<String> = emptySet(),
    val query: String = "",
    val page: Int = 0,
    val pageSize: Int = 20,
    val topMessage: String? = null
)

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonList: GetPokemonList,
    private val getPokemonDetail: GetPokemonDetail,
    private val searchPokemonUseCase: SearchPokemonUseCase,
    private val favoritesUseCase: FavoritesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonListState())
    val state: StateFlow<PokemonListState> = _state

    val favoritesFlow = favoritesUseCase.getFavoritesFlow()

    fun process(intent: PokemonListIntent) {
        when (intent) {
            is PokemonListIntent.Load -> loadFirstPage()
            is PokemonListIntent.Search -> search(intent.query)
            is PokemonListIntent.UpdateQuery -> _state.value = _state.value.copy(query = intent.query)
            is PokemonListIntent.ClearTopMessage -> _state.value = _state.value.copy(topMessage = null) // Clear top message
            is PokemonListIntent.LoadMore -> loadMore()
        }
    }

    private fun loadFirstPage() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, isLoadingMore = false, error = null, page = 0)
            val limit = _state.value.pageSize
            getPokemonList.getPokemonList(limit, 0).collect { result ->
                result.fold(
                    onSuccess = { data ->
                        _state.value = _state.value.copy(isLoading = false, isLoadingMore = false, data = data)
                        preGetPokemonDetails(data.results, limit)
                    }, onFailure = { throwable ->
                        var newState = _state.value.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            error = throwable.message ?: "Unknown error"
                        )
                        val domainError = ErrorMapper.map(throwable)
                        val userMessage = PresentationErrorMapper.toUserMessage(domainError)
                        newState = newState.copy(topMessage = userMessage)
                        _state.value = newState
                     })
            }
        }
    }

    private fun search(query: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                isLoadingMore = false,
                error = null,
                query = query,
                page = 0
            )
            val limit = _state.value.pageSize
            try {
                searchPokemonUseCase.search(query, limit, currentData = _state.value.data).collect { result ->
                    // ignore if user changed query
                    if (_state.value.query != query) return@collect

                    result.fold(onSuccess = { data ->
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            data = data,
                            error = null
                        )
                        preGetPokemonDetails(data.results, limit)
                    }, onFailure = { throwable ->
                        if (_state.value.query == query) {
                            var newState = _state.value.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                error = throwable.message ?: "Unknown error"
                            )
                            val domainError = ErrorMapper.map(throwable)
                            val userMessage = PresentationErrorMapper.toUserMessage(domainError)
                            newState = newState.copy(topMessage = userMessage)
                            _state.value = newState
                        }
                    })
                }
            } catch (e: Exception) {
                if (_state.value.query == query) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = e.message ?: "Unknown error",
                        topMessage = null
                    )
                 }
             }
         }
     }

    private fun loadMore() {
        viewModelScope.launch {
            // Prevent concurrent loadMore executions
            if (_state.value.isLoadingMore) return@launch

            val current = _state.value
            val baseData = current.data ?: return@launch

            if (current.query.isNotBlank().not()) {
                val nextPage = current.page + 1
                val limit = current.pageSize
                val offset = nextPage * limit

                _state.value = _state.value.copy(isLoadingMore = true, error = null)
                getPokemonList.getPokemonList(limit, offset).collect { result ->
                    result.fold(onSuccess = { data ->
                        // append results
                        val combined = baseData.results + data.results
                        _state.value = _state.value.copy(
                            isLoadingMore = false,
                            data = baseData.copy(results = combined),
                            page = nextPage
                        )
                        preGetPokemonDetails(data.results, limit)
                    }, onFailure = { throwable ->
                        var newState = _state.value.copy(
                            isLoadingMore = false,
                            error = throwable.message ?: "Unknown error"
                        )
                        val domainError = ErrorMapper.map(throwable)
                        val userMessage = PresentationErrorMapper.toUserMessage(domainError)
                        newState = newState.copy(topMessage = userMessage)
                        _state.value = newState
                     })
                }
            }
        }
    }

    fun getPokemonDetail(name: String): PokemonDetailModel? = _state.value.details[name]

    private suspend fun getPokemonDetailSuspend(name: String) {
        if (_state.value.details.containsKey(name) || _state.value.detailsLoading.contains(name)) return
        _state.value = _state.value.copy(detailsLoading = _state.value.detailsLoading + name)

        this@PokemonListViewModel.getPokemonDetail.getPokemonDetail(name).collect { result ->
            result.fold(
                onSuccess = { detail ->
                    _state.value = _state.value.copy(
                        details = _state.value.details + (name to detail),
                        detailsLoading = _state.value.detailsLoading - name
                    )
                }, onFailure = {
                    var newState = _state.value.copy(
                        detailsLoading = _state.value.detailsLoading - name
                    )
                    val domainError = ErrorMapper.map(it)
                    val userMessage = PresentationErrorMapper.toUserMessage(domainError)
                    newState = newState.copy(topMessage = userMessage)
                    _state.value = newState
                 }
             )
         }
     }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun preGetPokemonDetails(items: List<PokemonModel>, limit: Int) {
        val parallelism = 4
        viewModelScope.launch {
            items.asFlow()
                .take(limit)
                .flatMapMerge(concurrency = parallelism) { item ->
                    flowOf(item)
                }
                .collect { item ->
                    if (_state.value.details.containsKey(item.name)) return@collect
                    getPokemonDetailSuspend(item.name)
                }
        }
    }
}
