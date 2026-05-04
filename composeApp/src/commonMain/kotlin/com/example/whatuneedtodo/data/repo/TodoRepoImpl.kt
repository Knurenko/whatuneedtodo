package com.example.whatuneedtodo.data.repo

import com.example.whatuneedtodo.data.dao.TodoDao
import com.example.whatuneedtodo.data.entity.TodoEntity
import com.example.whatuneedtodo.data.mapper.toEntity
import com.example.whatuneedtodo.data.mapper.toModel
import com.example.whatuneedtodo.domain.model.TodoModel
import com.example.whatuneedtodo.domain.model.TodoPriority
import com.example.whatuneedtodo.domain.repo.TodoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.time.Clock

class TodoRepoImpl(
    private val todoDao: TodoDao
) : TodoRepo {
    override fun getTodos(): Flow<List<TodoModel>> {
        return todoDao.getTodos().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun addTodo(title: String, description: String, priority: TodoPriority) {
        val newEntity = TodoEntity(
            id = 0, // Room will auto-generate if id is 0
            title = title,
            description = description,
            priority = priority.name,
            creationDate = Clock.System.now().toEpochMilliseconds()
        )
        withContext(Dispatchers.IO) {
            todoDao.insertTodo(newEntity)
        }
    }

    override suspend fun removeTodo(itemToRemove: TodoModel) {
        withContext(Dispatchers.IO) {
            todoDao.deleteTodo(itemToRemove.toEntity())
        }
    }

    override suspend fun updateTodo(itemToUpdate: TodoModel) {
        withContext(Dispatchers.IO) {
            todoDao.updateTodo(itemToUpdate.toEntity())
        }
    }

    override suspend fun isTitleAlreadyExist(title: String): Boolean {
        return withContext(Dispatchers.IO) {
            todoDao.isTitleAlreadyExist(title)
        }
    }

    override suspend fun getTodoById(id: Int): TodoModel? {
        return withContext(Dispatchers.IO) {
            todoDao.getTodoById(id)?.toModel()
        }
    }
}
