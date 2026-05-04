package com.example.whatuneedtodo.ui.screens.edit

import com.example.whatuneedtodo.domain.repo.TodoRepo
import com.example.whatuneedtodo.ui.base.BaseViewModel
import com.example.whatuneedtodo.ui.screens.edit.EditContract.Action
import com.example.whatuneedtodo.ui.screens.edit.EditContract.UiState

class EditViewModel(
    private val repo: TodoRepo
) : BaseViewModel<UiState, Action>(UiState()) {

    private var currentItemId: Int = 0

    override suspend fun handleAction(action: Action) {
        when (action) {
            is Action.Init -> {
                currentItemId = action.itemId
                updateState { it.copy(isLoading = true) }
                val item = repo.getTodoById(action.itemId)
                if (item != null) {
                    updateState {
                        it.copy(
                            isLoading = false,
                            initialTitle = item.title,
                            initialDesc = item.description,
                            initialPriority = item.priority
                        )
                    }
                } else {
                    // Handle error or navigate back
                    updateState { it.copy(isLoading = false) }
                }
            }

            is Action.SaveChanges -> {
                val itemToUpdate = repo.getTodoById(currentItemId)?.copy(
                    title = action.newTitle,
                    description = action.newDescription,
                    priority = action.newPriority
                )
                if (itemToUpdate != null) {
                    repo.updateTodo(itemToUpdate)
                    updateState { it.copy(isSuccessfullyEdited = true) }
                }
            }
        }
    }
}
