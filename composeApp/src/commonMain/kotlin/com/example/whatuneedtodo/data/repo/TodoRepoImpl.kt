package com.example.whatuneedtodo.data.repo

import com.example.whatuneedtodo.data.dao.TodoDao
import com.example.whatuneedtodo.data.mapper.toEntity
import com.example.whatuneedtodo.data.mapper.toModel
import com.example.whatuneedtodo.domain.model.TodoModel
import com.example.whatuneedtodo.domain.repo.TodoRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepoImpl(
    private val todoDao: TodoDao
) : TodoRepo {
    override suspend fun getTodos(): Flow<List<TodoModel>> {
        return todoDao.getTodos().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun addTodo(itemToAdd: TodoModel) {
        todoDao.insertTodo(itemToAdd.toEntity())
    }

    override suspend fun removeTodo(itemToRemove: TodoModel) {
        todoDao.deleteTodo(itemToRemove.toEntity())
    }

    override suspend fun updateTodo(itemToUpdate: TodoModel) {
        todoDao.updateTodo(itemToUpdate.toEntity())
    }
}
