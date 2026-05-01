package com.example.whatuneedtodo.ui.screens.add_new

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.whatuneedtodo.ui.navigation.LocalNavigation
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddNewController() {
    val vm: AddNewViewModel = koinViewModel()
    val state by vm.state.collectAsStateWithLifecycle()
    val navigation = LocalNavigation.current

    LaunchedEffect(state.isSuccessfullyAdded) {
        if (state.isSuccessfullyAdded) {
            delay(500)
            navigation.navigateUp()
        }
    }

    AddNewScreen(state = state, sendAction = vm::catchAction, onNavAction = {
        when (it) {
            AddNewContract.NavAction.Close -> { navigation.navigateUp() }
        }
    })
}