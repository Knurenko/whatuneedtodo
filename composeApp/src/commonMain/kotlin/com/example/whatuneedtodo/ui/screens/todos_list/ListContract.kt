package com.example.whatuneedtodo.ui.screens.todos_list

import androidx.compose.runtime.Immutable
import com.example.whatuneedtodo.domain.model.TodoModel

interface ListContract {
    @Immutable
    data class UiState(
        val itemsToShow: List<TodoModel> = emptyList(),
        val searchQuery: String = "",
        val sortType: SortType = SortType.ByDateNewFirst
    )

    sealed interface Action {
        data class ChangeSortType(val newSortType: SortType) : Action
        data class SearchByQuery(val query: String) : Action
        data class DeleteItem(val itemToDelete: TodoModel) : Action
    }

    sealed interface NavAction {
        data object NavigateToAddNew : NavAction
        data class NavigateToEdit(val id: Int) : NavAction
    }

    enum class SortType {
        ByDateOldFirst,
        ByDateNewFirst,
        ByPriorityUrgentFirst,
        ByPriorityLowFirst
    }
}