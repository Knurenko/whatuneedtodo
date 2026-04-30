package com.example.whatuneedtodo.di.modules

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.whatuneedtodo.data.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.core.scope.Scope
import java.io.File

private fun getDBBuilder() : RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "todos.db")
    return Room.databaseBuilder<AppDatabase>(dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
}

actual fun getBuilder(diScope: Scope): RoomDatabase.Builder<AppDatabase> = getDBBuilder()