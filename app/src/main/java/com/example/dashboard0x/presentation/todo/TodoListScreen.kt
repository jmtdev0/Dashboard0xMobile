package com.example.dashboard0x.presentation.todo

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.dashboard0x.data.model.todo.Todo
import com.example.dashboard0x.presentation.components.EmptyState
import com.example.dashboard0x.presentation.components.ErrorMessage
import com.example.dashboard0x.presentation.components.LoadingIndicator
import com.example.dashboard0x.presentation.todo.components.AddTodoDialog
import com.example.dashboard0x.presentation.todo.components.EditTodoDialog
import com.example.dashboard0x.presentation.todo.components.TodoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    viewModel: TodoViewModel,
    onNavigateToPrivateDashboard: () -> Unit,
    onNavigateToPublicDashboard: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var todoToEdit by remember { mutableStateOf<Todo?>(null) }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TODOs") },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Private Dashboard") },
                            onClick = {
                                showMenu = false
                                onNavigateToPrivateDashboard()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Public Dashboard") },
                            onClick = {
                                showMenu = false
                                onNavigateToPublicDashboard()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                showMenu = false
                                onLogout()
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add TODO")
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading && uiState.todos.isEmpty() -> {
                LoadingIndicator(modifier = Modifier.padding(paddingValues))
            }
            uiState.error != null && uiState.todos.isEmpty() -> {
                ErrorMessage(
                    message = uiState.error ?: "Unknown error",
                    onRetry = { viewModel.loadTodos() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            uiState.todos.isEmpty() -> {
                EmptyState(
                    message = "No TODOs yet. Add one to get started!",
                    modifier = Modifier.padding(paddingValues)
                )
            }
            else -> {
                PullToRefreshBox(
                    isRefreshing = uiState.isLoading,
                    onRefresh = { viewModel.loadTodos() },
                    modifier = Modifier.padding(paddingValues)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.todos, key = { it.id }) { todo ->
                            TodoItem(
                                todo = todo,
                                onCheckedChange = { completed ->
                                    viewModel.updateTodo(todo.id, completed = completed)
                                },
                                onEditClick = { todoToEdit = todo },
                                onDeleteClick = { viewModel.deleteTodo(todo.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddTodoDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { text ->
                viewModel.createTodo(text)
                showAddDialog = false
            }
        )
    }

    todoToEdit?.let { todo ->
        EditTodoDialog(
            todo = todo,
            onDismiss = { todoToEdit = null },
            onConfirm = { newText ->
                viewModel.updateTodo(todo.id, text = newText)
                todoToEdit = null
            }
        )
    }
}
