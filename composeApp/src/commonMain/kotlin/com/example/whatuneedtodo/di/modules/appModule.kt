package com.example.whatuneedtodo.di.modules

import com.example.whatuneedtodo.data.repo.TodoRepoImpl
import com.example.whatuneedtodo.domain.repo.TodoRepo
import com.example.whatuneedtodo.ui.screens.add_new.AddNewViewModel
import com.example.whatuneedtodo.ui.screens.todos_list.ListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { TodoRepoImpl(todoDao = get()) } bind TodoRepo::class
    
    viewModelOf(::AddNewViewModel)
    viewModelOf(::ListViewModel)
}