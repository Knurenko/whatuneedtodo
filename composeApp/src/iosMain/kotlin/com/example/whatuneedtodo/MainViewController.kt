package com.example.whatuneedtodo

import androidx.compose.ui.window.ComposeUIViewController
import com.example.whatuneedtodo.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }