package com.example.whatuneedtodo.data.mapper

import com.example.whatuneedtodo.data.entity.TodoEntity
import com.example.whatuneedtodo.domain.model.TodoModel
import com.example.whatuneedtodo.domain.model.TodoPriority

fun TodoEntity.toModel(): TodoModel {
    return TodoModel(
        id = id,
        title = title,
        description = description,
        priority = TodoPriority.valueOf(priority),
        creationDate = creationDate
    )
}

fun TodoModel.toEntity(): TodoEntity {
    return TodoEntity(
        id = id,
        title = title,
        description = description,
        priority = priority.name,
        creationDate = creationDate
    )
}
