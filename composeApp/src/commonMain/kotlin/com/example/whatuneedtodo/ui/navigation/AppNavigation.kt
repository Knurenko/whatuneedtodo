package com.example.whatuneedtodo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whatuneedtodo.ui.screens.add_new.AddNewController
import com.example.whatuneedtodo.ui.screens.todos_list.ListController
import com.example.whatuneedtodo.ui.screens.todos_list.ListContract
import com.example.whatuneedtodo.utils.Placeholders

sealed class Screen(val route: String) {
    data object List : Screen("list")
    data object AddNew : Screen("add_new")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavigation provides navController) {
        NavHost(
            navController = navController,
            startDestination = Screen.List.route
        ) {
            composable(Screen.List.route) {
                ListController()
            }
            composable(Screen.AddNew.route) {
                AddNewController()
            }
        }
    }
}

val LocalNavigation = staticCompositionLocalOf<NavHostController> {
    error("No LocalNavigation provided")
}