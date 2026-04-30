package com.example.whatuneedtodo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.whatuneedtodo.ui.navigation.AppNavigation

/**
 * It gonna be our main init point
 */
@Composable
@Preview
fun App() {
    MaterialTheme {
        AppNavigation()
    }
}