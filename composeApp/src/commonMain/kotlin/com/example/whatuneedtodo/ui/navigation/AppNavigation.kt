package com.example.whatuneedtodo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whatuneedtodo.ui.screens.todos_list.ListScreen
import com.example.whatuneedtodo.ui.screens.add_new.AddNewScreen
import com.example.whatuneedtodo.ui.screens.todos_list.ListContract
import com.example.whatuneedtodo.ui.screens.add_new.AddNewContract
import com.example.whatuneedtodo.utils.Placeholders

sealed class Screen(val route: String) {
    data object List : Screen("list")
    data object AddNew : Screen("add_new")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.List.route
    ) {
        composable(Screen.List.route) {
            ListScreen(
                state = ListContract.UiState(itemsToShow = Placeholders.todoList),
                sendAction = {},
                onNavAction = { action ->
                    when (action) {
                        ListContract.NavAction.NavigateToAddNew -> navController.navigate(Screen.AddNew.route)
                    }
                }
            )
        }
        composable(Screen.AddNew.route) {
            AddNewScreen(
                state = AddNewContract.UiState(),
                sendAction = {},
                onNavAction = { action ->
                    when (action) {
                        AddNewContract.NavAction.Close -> navController.popBackStack()
                    }
                }
            )
        }
    }
}
