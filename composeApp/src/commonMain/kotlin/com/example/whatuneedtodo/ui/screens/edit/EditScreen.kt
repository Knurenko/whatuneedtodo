package com.example.whatuneedtodo.ui.screens.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.whatuneedtodo.domain.model.TodoPriority
import com.example.whatuneedtodo.ui.screens.edit.EditContract.Action
import com.example.whatuneedtodo.ui.screens.edit.EditContract.NavAction
import com.example.whatuneedtodo.ui.screens.edit.EditContract.UiState
import org.jetbrains.compose.resources.stringResource
import whatuneedtodo.composeapp.generated.resources.Res
import whatuneedtodo.composeapp.generated.resources.apply_button
import whatuneedtodo.composeapp.generated.resources.cancel_button
import whatuneedtodo.composeapp.generated.resources.description_label
import whatuneedtodo.composeapp.generated.resources.priority_label
import whatuneedtodo.composeapp.generated.resources.title_label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    state: UiState,
    sendAction: (Action) -> Unit,
    sendNavAction: (NavAction) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(TodoPriority.Medium) }

    val focusManager = LocalFocusManager.current

    // Initialize values when data is loaded
    LaunchedEffect(state.isLoading) {
        if (!state.isLoading) {
            title = state.initialTitle
            description = state.initialDesc
            priority = state.initialPriority
        }
    }

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(Res.string.title_label)) },
                    modifier = Modifier.fillMaxWidth(),
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
                    onClick = { sendAction(Action.SaveChanges(title, description, priority)) },
                    enabled = title.isNotBlank() && !state.isSuccessfullyEdited,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.isSuccessfullyEdited) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    } else {
                        Text(stringResource(Res.string.apply_button))
                    }
                }

                OutlinedButton(
                    onClick = { sendNavAction(NavAction.GoBack) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.cancel_button))
                }
            }
        }
    }
}