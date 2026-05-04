package com.example.whatuneedtodo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.whatuneedtodo.ui.screens.add_new.AddNewController
import com.example.whatuneedtodo.ui.screens.edit.EditController
import com.example.whatuneedtodo.ui.screens.todos_list.ListController
import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object List : Screen
    @Serializable
    data object AddNew : Screen
    @Serializable
    data class Edit(val id: Int) : Screen
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavigation provides navController) {
        NavHost(
            navController = navController,
            startDestination = Screen.List
        ) {
            composable<Screen.List> {
                ListController()
            }
            composable<Screen.AddNew> {
                AddNewController()
            }
            composable<Screen.Edit> { navBackStackEntry ->
                val route = navBackStackEntry.toRoute<Screen.Edit>()
                val itemId = route.id
                EditController(itemId = itemId)
            }
        }
    }
}

val LocalNavigation = staticCompositionLocalOf<NavHostController> {
    error("No LocalNavigation provided")
}