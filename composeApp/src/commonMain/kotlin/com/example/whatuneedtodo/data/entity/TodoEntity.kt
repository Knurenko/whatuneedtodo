package com.example.whatuneedtodo.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.whatuneedtodo.domain.model.TodoPriority

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: String, // Storing enum as String
    val creationDate: Long
)
