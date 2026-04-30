package com.example.whatuneedtodo.di

import com.example.whatuneedtodo.di.modules.appModule
import com.example.whatuneedtodo.di.modules.databaseModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

private val activeModules : List<Module>
    get() = listOf(
        appModule,
        databaseModule
    )

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(activeModules)
    }
}