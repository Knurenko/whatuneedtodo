package com.example.whatuneedtodo.ui.screens.add_new

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.example.whatuneedtodo.domain.model.TodoPriority
import com.example.whatuneedtodo.ui.screens.add_new.AddNewContract.*
import org.jetbrains.compose.resources.stringResource
import whatuneedtodo.composeapp.generated.resources.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewScreen(state: UiState, sendAction: (Action) -> Unit, onNavAction: (NavAction) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(TodoPriority.Medium) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                sendAction(Action.CheckIfTitleAlreadyExist(it))
            },
            label = { Text(stringResource(Res.string.title_label)) },
            isError = state.isTitleAlreadyExist,
            supportingText = if (state.isTitleAlreadyExist) {
                { Text(stringResource(Res.string.title_exists_error)) }
            } else null,
            trailingIcon = if (state.isCheckInProgress) {
                {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
            } else null,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(Res.string.description_label)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = priority.name,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(Res.string.priority_label)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    )
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                TodoPriority.entries.forEach { p ->
                    DropdownMenuItem(
                        text = { Text(p.name) },
                        onClick = {
                            priority = p
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { sendAction(Action.Apply(title, description, priority)) },
            enabled = state.isTitleInputted && !state.isTitleAlreadyExist && !state.isSuccessfullyAdded,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isSuccessfullyAdded) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(Res.string.successfully_added_description)
                )
            } else {
                Text(stringResource(Res.string.apply_button))
            }
        }

        OutlinedButton(
            onClick = { onNavAction(NavAction.Close) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.cancel_button))
        }
    }
}

@Preview
@Composable
private fun Preview() = MaterialTheme {
    val scope = rememberCoroutineScope()
    var state by remember { mutableStateOf(UiState()) }
    val banList = listOf(
        "todo1",
        "test",
        "asd"
    )

    AddNewScreen(
        state = state,
        sendAction = {
            when (it) {
                is Action.Apply -> {
                    state = state.copy(isSuccessfullyAdded = true)
                }

                is Action.CheckIfTitleAlreadyExist -> {
                    state = state.copy(isCheckInProgress = true)
                    scope.launch {
                        delay(2_000L)
                        val isExist = banList.contains(it.title)
                        state = state.copy(
                            isTitleInputted = it.title.isNotBlank(),
                            isTitleAlreadyExist = isExist,
                            isCheckInProgress = false
                        )
                    }
                }
            }
        },
        onNavAction = {}
    )
}