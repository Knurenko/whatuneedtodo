package com.example.whatuneedtodo.domain.model

data class TodoModel(
    val id: Int,
    val title: String,
    val creationDate: Long,
    val priority: TodoPriority,
    val description: String
)
