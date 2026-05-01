package com.example.whatuneedtodo.ui.screens.add_new

import com.example.whatuneedtodo.domain.repo.TodoRepo
import com.example.whatuneedtodo.ui.base.BaseViewModel
import com.example.whatuneedtodo.ui.screens.add_new.AddNewContract.Action
import com.example.whatuneedtodo.ui.screens.add_new.AddNewContract.UiState

class AddNewViewModel(
    private val repo: TodoRepo
) : BaseViewModel<UiState, Action>(UiState()) {

    override suspend fun handleAction(action: Action) {
        when (action) {
            is Action.Apply -> {
                repo.addTodo(
                    title = action.title,
                    description = action.description,
                    priority = action.priority
                )
                updateState { it.copy(isSuccessfullyAdded = true) }
            }

            is Action.CheckIfTitleAlreadyExist -> {
                updateState { it.copy(isCheckInProgress = true) }
                val isAlreadyExist = repo.isTitleAlreadyExist(action.title)
                updateState {
                    it.copy(
                        isTitleAlreadyExist = isAlreadyExist,
                        isTitleInputted = action.title.isNotEmpty(),
                        isCheckInProgress = false
                    )
                }
            }
        }
    }
}