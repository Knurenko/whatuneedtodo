package com.example.whatuneedtodo.di.modules

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.whatuneedtodo.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

actual fun getBuilder(diScope: Scope): RoomDatabase.Builder<AppDatabase> {
    val context = diScope.androidContext()
    val dbFile = context.applicationContext.getDatabasePath("todos.db")

    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}