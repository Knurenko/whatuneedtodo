package com.example.whatuneedtodo.domain.repo

import com.example.whatuneedtodo.domain.model.TodoModel
import kotlinx.coroutines.flow.Flow

interface TodoRepo {
    suspend fun getTodos(): Flow<List<TodoModel>>
    suspend fun addTodo(itemToAdd: TodoModel)
    suspend fun removeTodo(itemToRemove: TodoModel)
    suspend fun updateTodo(itemToUpdate: TodoModel)
}