package com.example.whatuneedtodo.ui.screens.todos_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.whatuneedtodo.ui.navigation.LocalNavigation
import com.example.whatuneedtodo.ui.navigation.Screen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ListController() {
    val vm: ListViewModel = koinViewModel()
    val state by vm.state.collectAsStateWithLifecycle()
    val navigation = LocalNavigation.current

    ListScreen(
        state = state,
        sendAction = vm::catchAction,
        onNavAction = { action ->
            when (action) {
                ListContract.NavAction.NavigateToAddNew -> {
                    navigation.navigate(Screen.AddNew.route)
                }
            }
        }
    )
}
