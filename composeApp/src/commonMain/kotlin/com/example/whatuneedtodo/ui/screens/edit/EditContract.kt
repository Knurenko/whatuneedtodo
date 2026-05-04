package com.example.whatuneedtodo.ui.screens.edit

import androidx.compose.runtime.Immutable
import com.example.whatuneedtodo.domain.model.TodoPriority

interface EditContract {
    @Immutable
    data class UiState(
        val isTitleAlreadyExist: Boolean = false,
        val isSuccessfullyEdited: Boolean = false,
        val isLoading: Boolean = true,
        val initialTitle: String = "",
        val initialDesc: String = "",
        val initialPriority: TodoPriority = TodoPriority.Medium
    )

    sealed interface Action {
        data class SaveChanges(
            val newTitle: String,
            val newDescription: String,
            val newPriority: TodoPriority
        ) : Action

        data class Init(val itemId: Int) : Action
    }

    sealed interface NavAction {
        data object GoBack : NavAction
    }
}