package com.example.dashboard0x.data.model.todo

data class Todo(
    val id: String,
    val text: String,
    val completed: Boolean,
    val createdAt: String,
    val completedAt: String? = null
)
