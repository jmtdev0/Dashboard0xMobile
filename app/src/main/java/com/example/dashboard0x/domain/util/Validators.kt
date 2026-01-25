package com.example.dashboard0x.domain.util

import com.example.dashboard0x.util.Constants

object Validators {
    fun validateTodoText(text: String): String? = when {
        text.isBlank() -> "Text cannot be empty"
        text.length > Constants.TODO_MAX_LENGTH -> "Text too long (max ${Constants.TODO_MAX_LENGTH} characters)"
        else -> null
    }

    fun validatePassword(password: String): String? =
        if (password.isBlank()) "Password cannot be empty" else null
}
