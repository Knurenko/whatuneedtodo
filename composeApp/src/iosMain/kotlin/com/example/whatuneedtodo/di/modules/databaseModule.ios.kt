package com.example.whatuneedtodo.di.modules

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.whatuneedtodo.data.db.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.scope.Scope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

private fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbPath = documentDirectory() + "/todos.db"
    return Room.databaseBuilder<AppDatabase>(name = dbPath)
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val docDir = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )

    return requireNotNull(docDir?.path)
}
actual fun getBuilder(diScope: Scope): RoomDatabase.Builder<AppDatabase> = getDatabaseBuilder()