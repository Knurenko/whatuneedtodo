package com.example.whatuneedtodo.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.example.whatuneedtodo.data.dao.TodoDao
import com.example.whatuneedtodo.data.entity.TodoEntity
import com.example.whatuneedtodo.di.modules.AppDatabaseConstructor

@Database(entities = [TodoEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}