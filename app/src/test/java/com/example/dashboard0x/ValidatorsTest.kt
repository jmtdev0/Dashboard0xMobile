package com.example.dashboard0x

import com.example.dashboard0x.domain.util.Validators
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidatorsTest {

    @Test
    fun `validateTodoText with valid text returns null`() {
        val result = Validators.validateTodoText("Valid TODO text")
        assertNull(result)
    }

    @Test
    fun `validateTodoText with empty text returns error`() {
        val result = Validators.validateTodoText("")
        assertEquals("Text cannot be empty", result)
    }

    @Test
    fun `validateTodoText with blank text returns error`() {
        val result = Validators.validateTodoText("   ")
        assertEquals("Text cannot be empty", result)
    }

    @Test
    fun `validateTodoText with text exceeding max length returns error`() {
        val longText = "a".repeat(501)
        val result = Validators.validateTodoText(longText)
        assertEquals("Text too long (max 500 characters)", result)
    }

    @Test
    fun `validateTodoText with text at max length returns null`() {
        val maxText = "a".repeat(500)
        val result = Validators.validateTodoText(maxText)
        assertNull(result)
    }

    @Test
    fun `validatePassword with valid password returns null`() {
        val result = Validators.validatePassword("mySecurePassword123")
        assertNull(result)
    }

    @Test
    fun `validatePassword with empty password returns error`() {
        val result = Validators.validatePassword("")
        assertEquals("Password cannot be empty", result)
    }

    @Test
    fun `validatePassword with blank password returns error`() {
        val result = Validators.validatePassword("   ")
        assertEquals("Password cannot be empty", result)
    }
}
