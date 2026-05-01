package com.example.whatuneedtodo.ui.screens.todos_list

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.whatuneedtodo.domain.model.TodoModel
import com.example.whatuneedtodo.domain.model.TodoPriority
import com.example.whatuneedtodo.ui.screens.todos_list.ListContract.*
import com.example.whatuneedtodo.utils.Placeholders
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import whatuneedtodo.composeapp.generated.resources.*
import kotlin.math.roundToInt

@Composable
fun ListScreen(
    state: UiState,
    sendAction: (Action) -> Unit,
    onNavAction: (NavAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    var itemPendingDelete by remember { mutableStateOf<TodoModel?>(null) }

    LaunchedEffect(state.sortType) {
        if (state.itemsToShow.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    itemPendingDelete?.let { item ->
        DeleteConfirmationDialog(
            item = item,
            onConfirm = {
                sendAction(Action.DeleteItem(item))
                itemPendingDelete = null
            },
            onDismiss = { itemPendingDelete = null }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavAction(NavAction.NavigateToAddNew) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(Res.string.add_task_content_description)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchAndSortBar(
                query = state.searchQuery,
                currentSort = state.sortType,
                onQueryChange = { sendAction(Action.SearchByQuery(it)) },
                onSortSelected = { sendAction(Action.ChangeSortType(it)) }
            )

            if (state.itemsToShow.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val message = if (state.searchQuery.isEmpty()) {
                        stringResource(Res.string.empty_db_message)
                    } else {
                        stringResource(Res.string.empty_search_message)
                    }
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(32.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        items = state.itemsToShow,
                        key = { it.id }
                    ) { item ->
                        TodoItemRow(
                            item = item,
                            onDeleteRequest = { itemPendingDelete = it }
                        )
                    }
                }
            }
        }
    }
}

// ... update other components in the same file ...

// ─────────────────────────────────────────────
// Search + Sort bar
// ─────────────────────────────────────────────

@Composable
private fun SearchAndSortBar(
    query: String,
    currentSort: SortType,
    onQueryChange: (String) -> Unit,
    onSortSelected: (SortType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text(stringResource(Res.string.search_task_placeholder)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.context_menu_cancel))
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )

        Spacer(Modifier.width(8.dp))

        SortMenuButton(currentSort = currentSort, onSortSelected = onSortSelected)
    }
}

@Composable
private fun SortMenuButton(
    currentSort: SortType,
    onSortSelected: (SortType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = stringResource(Res.string.sort_content_description),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SortType.entries.forEach { sort ->
                DropdownMenuItem(
                    text = { Text(sort.label) },
                    onClick = {
                        onSortSelected(sort)
                        expanded = false
                    },
                    leadingIcon = {
                        if (sort == currentSort) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Spacer(Modifier.size(24.dp))
                        }
                    }
                )
            }
        }
    }
}

private val SortType.label: String
    @Composable
    get() = when (this) {
        SortType.ByDateNewFirst -> stringResource(Res.string.sort_newest_first)
        SortType.ByDateOldFirst -> stringResource(Res.string.sort_oldest_first)
        SortType.ByPriorityUrgentFirst -> stringResource(Res.string.sort_urgent_first)
        SortType.ByPriorityLowFirst -> stringResource(Res.string.sort_low_priority_first)
    }

enum class DragAnchors { Start, End }

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TodoItemRow(
    item: TodoModel,
    onDeleteRequest: (TodoModel) -> Unit
) {
    var showContextMenu by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    val anchors = remember {
        DraggableAnchors {
            DragAnchors.Start at 0f
            DragAnchors.End at -density.run { 80.dp.toPx() }
        }
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
        )
    }

    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = state,
        positionalThreshold = { distance -> distance * 0.5f },
        animationSpec = tween(),
    )

    LaunchedEffect(anchors) {
        state.updateAnchors(anchors)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .anchoredDraggable(
                state = state,
                orientation = Orientation.Horizontal,
                flingBehavior = flingBehavior
            )
    ) {
        // Background with delete icon
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.errorContainer),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(
                onClick = {
                    onDeleteRequest(item)
                },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(Res.string.context_menu_delete),
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Foreground content
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    val offset = if (state.offset.isNaN()) 0f else state.offset
                    IntOffset(offset.roundToInt(), 0)
                }
                .padding(horizontal = 16.dp)
                .combinedClickable(
                    onClick = { /* details / edit — coming soon */ },
                    onLongClick = { showContextMenu = true }
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            TodoItemContent(item)
        }

        ItemContextMenu(
            expanded = showContextMenu,
            onDismiss = { showContextMenu = false },
            onUpdate = {
                showContextMenu = false
                // TODO: onNavAction(NavAction.NavigateToEdit(item.id))
            },
            onDelete = {
                showContextMenu = false
                onDeleteRequest(item)
            }
        )
    }
}

@Composable
private fun TodoItemContent(item: TodoModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (item.description.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = item.creationDate.toFormattedDate(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
        PriorityBadge(item.priority)
    }
}

@Composable
private fun PriorityBadge(priority: TodoPriority) {
    val (label, color) = when (priority) {
        TodoPriority.Urgent -> stringResource(Res.string.sort_urgent_first).split(" ").first() to Color(0xFFD32F2F)
        TodoPriority.High -> "High" to Color(0xFFF57C00)
        TodoPriority.Medium -> "Medium" to Color(0xFF388E3C)
        TodoPriority.Low -> stringResource(Res.string.sort_low_priority_first).split(" ").first() to Color(0xFF455A64)
    }
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

// ─────────────────────────────────────────────
// Context menu  (long-click)
// ─────────────────────────────────────────────

@Composable
private fun ItemContextMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onUpdate: () -> Unit,
    onDelete: () -> Unit
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        DropdownMenuItem(
            text = { Text(stringResource(Res.string.context_menu_update)) },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
            onClick = onUpdate
        )
        DropdownMenuItem(
            text = { Text(stringResource(Res.string.context_menu_delete), color = MaterialTheme.colorScheme.error) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            onClick = onDelete
        )
    }
}

// ─────────────────────────────────────────────
// Delete confirmation dialog
// ─────────────────────────────────────────────

@Composable
private fun DeleteConfirmationDialog(
    item: TodoModel,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = { Text(stringResource(Res.string.delete_dialog_title)) },
        text = { Text(stringResource(Res.string.delete_dialog_message, item.title)) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) { Text(stringResource(Res.string.context_menu_delete)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(Res.string.context_menu_cancel)) }
        }
    )
}

private fun Long.toFormattedDate(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val month = localDateTime.monthNumber.toString().padStart(2, '0')
    val year = localDateTime.year
    return "$day.$month.$year"
}

@Preview
@Composable
private fun Preview() = MaterialTheme {
    var state: UiState by remember { mutableStateOf(UiState(itemsToShow = Placeholders.todoList)) }
    val onAction: (Action) -> Unit = {
        when (it) {
            is Action.ChangeSortType -> {
                val sorted = when (it.newSortType) {
                    SortType.ByDateOldFirst -> state.itemsToShow.sortedBy { i -> i.creationDate }
                    SortType.ByDateNewFirst -> state.itemsToShow.sortedByDescending { i -> i.creationDate }
                    SortType.ByPriorityUrgentFirst -> state.itemsToShow.sortedByDescending { i -> i.priority }
                    SortType.ByPriorityLowFirst -> state.itemsToShow.sortedBy { i -> i.priority }
                }
                state = state.copy(sortType = it.newSortType, itemsToShow = sorted)
            }

            is Action.DeleteItem -> {
                val updatedList =
                    state.itemsToShow.filterNot { item -> item.id == it.itemToDelete.id }
                state = state.copy(itemsToShow = updatedList)
            }

            is Action.SearchByQuery -> {
                val filtered = state.itemsToShow.filter { item -> item.title.contains(it.query) }
                state = state.copy(searchQuery = it.query, itemsToShow = filtered)
            }
        }
    }

    ListScreen(state = state, sendAction = onAction, onNavAction = {})
}