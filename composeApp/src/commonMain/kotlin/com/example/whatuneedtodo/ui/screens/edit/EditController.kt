package com.example.whatuneedtodo.ui.screens.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.whatuneedtodo.ui.navigation.LocalNavigation
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditController(itemId: Int) {
    val vm: EditViewModel = koinViewModel()
    val state by vm.state.collectAsStateWithLifecycle()
    val navigation = LocalNavigation.current

    LaunchedEffect(itemId) {
        vm.catchAction(EditContract.Action.Init(itemId))
    }

    LaunchedEffect(state.isSuccessfullyEdited) {
        if (state.isSuccessfullyEdited) {
            delay(500)
            navigation.navigateUp()
        }
    }

    EditScreen(
        state = state,
        sendAction = vm::catchAction,
        sendNavAction = { navAction ->
            when (navAction) {
                EditContract.NavAction.GoBack -> navigation.navigateUp()
            }
        }
    )
}
