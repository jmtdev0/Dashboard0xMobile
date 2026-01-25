package com.example.dashboard0x.data.model.todo

data class UpdateTodoRequest(
    val id: String,
    val text: String? = null,
    val completed: Boolean? = null
)
