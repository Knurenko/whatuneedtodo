package com.example.whatuneedtodo.ui.screens.add_new

import com.example.whatuneedtodo.domain.model.TodoPriority

interface AddNewContract {
    data class UiState(
        val isTitleInputted: Boolean = false,
        val isTitleAlreadyExist: Boolean = false,
        val isCheckInProgress: Boolean = false,
        val isSuccessfullyAdded: Boolean = false
    )

    sealed interface Action {
        data class Apply(
            val title: String,
            val description: String,
            val priority: TodoPriority,
        ) : Action

        data class CheckIfTitleAlreadyExist(val title: String) : Action
    }

    sealed interface NavAction {
        data object Close : NavAction
    }
}