package com.example.whatuneedtodo.ui.screens.todos_list

import androidx.lifecycle.viewModelScope
import com.example.whatuneedtodo.domain.repo.TodoRepo
import com.example.whatuneedtodo.ui.base.BaseViewModel
import com.example.whatuneedtodo.ui.screens.todos_list.ListContract.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ListViewModel(
    private val repo: TodoRepo
) : BaseViewModel<UiState, Action>(UiState()) {

    init {
        val searchQueryFlow = state.map { it.searchQuery }.distinctUntilChanged()
        val sortTypeFlow = state.map { it.sortType }.distinctUntilChanged()

        combine(
            repo.getTodos(),
            searchQueryFlow,
            sortTypeFlow
        ) { todos, query, sort ->
            val filtered = if (query.isBlank()) {
                todos
            } else {
                todos.filter { 
                    it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
                }
            }

            val sorted = when (sort) {
                SortType.ByDateOldFirst -> filtered.sortedBy { it.creationDate }
                SortType.ByDateNewFirst -> filtered.sortedByDescending { it.creationDate }
                SortType.ByPriorityUrgentFirst -> filtered.sortedByDescending { it.priority }
                SortType.ByPriorityLowFirst -> filtered.sortedBy { it.priority }
            }
            
            sorted
        }.onEach { items ->
            updateState { it.copy(itemsToShow = items) }
        }.launchIn(viewModelScope)
    }

    override suspend fun handleAction(action: Action) {
        when (action) {
            is Action.ChangeSortType -> {
                updateState { it.copy(sortType = action.newSortType) }
            }
            is Action.SearchByQuery -> {
                updateState { it.copy(searchQuery = action.query) }
            }
            is Action.DeleteItem -> {
                repo.removeTodo(action.itemToDelete)
            }
        }
    }
}
