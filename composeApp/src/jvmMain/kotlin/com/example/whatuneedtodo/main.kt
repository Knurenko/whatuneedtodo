package com.example.whatuneedtodo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.whatuneedtodo.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "WhatUNeedToDo",
    ) {
        App()
    }
}