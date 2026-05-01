package com.example.whatuneedtodo.domain.repo

import com.example.whatuneedtodo.domain.model.TodoModel
import com.example.whatuneedtodo.domain.model.TodoPriority
import kotlinx.coroutines.flow.Flow

interface TodoRepo {
    fun getTodos(): Flow<List<TodoModel>>
    suspend fun addTodo(title: String, description: String, priority: TodoPriority)
    suspend fun removeTodo(itemToRemove: TodoModel)
    suspend fun updateTodo(itemToUpdate: TodoModel)
    suspend fun isTitleAlreadyExist(title: String): Boolean
}