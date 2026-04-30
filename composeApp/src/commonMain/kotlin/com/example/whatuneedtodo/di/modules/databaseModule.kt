package com.example.whatuneedtodo.di.modules

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.whatuneedtodo.data.dao.TodoDao
import com.example.whatuneedtodo.data.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.scope.Scope
import org.koin.dsl.module

expect fun getBuilder(diScope: Scope) : RoomDatabase.Builder<AppDatabase>

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor: RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

val databaseModule = module {
    single<AppDatabase> {
        getBuilder(diScope = this)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single<TodoDao> {
        val db: AppDatabase = get()
        db.todoDao()
    }
}