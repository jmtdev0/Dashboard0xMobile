package com.example.dashboard0x.presentation.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboard0x.data.model.todo.Todo
import com.example.dashboard0x.data.repository.TodoRepository
import com.example.dashboard0x.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    init {
        loadTodos()
    }

    fun loadTodos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = todoRepository.getTodos()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        todos = result.data?.todos ?: emptyList()
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> {}
            }
        }
    }

    fun createTodo(text: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreating = true, error = null)
            when (val result = todoRepository.createTodo(text)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(isCreating = false)
                    loadTodos() // Reload list after creating
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
                else -> {}
            }
        }
    }

    fun updateTodo(id: String, text: String? = null, completed: Boolean? = null) {
        viewModelScope.launch {
            when (val result = todoRepository.updateTodo(id, text, completed)) {
                is Resource.Success -> {
                    loadTodos() // Reload list after updating
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(error = result.message)
                }
                else -> {}
            }
        }
    }

    fun deleteTodo(id: String) {
        viewModelScope.launch {
            when (val result = todoRepository.deleteTodo(id)) {
                is Resource.Success -> {
                    loadTodos() // Reload list after deleting
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(error = result.message)
                }
                else -> {}
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class TodoUiState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val isCreating: Boolean = false,
    val error: String? = null
)
