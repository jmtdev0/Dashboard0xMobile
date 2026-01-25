package com.example.dashboard0x.data.repository

import com.example.dashboard0x.data.local.TokenManager
import com.example.dashboard0x.data.model.auth.ErrorResponse
import com.example.dashboard0x.data.model.todo.CreateTodoRequest
import com.example.dashboard0x.data.model.todo.DeleteResponse
import com.example.dashboard0x.data.model.todo.DeleteTodoRequest
import com.example.dashboard0x.data.model.todo.Todo
import com.example.dashboard0x.data.model.todo.TodoListData
import com.example.dashboard0x.data.model.todo.UpdateTodoRequest
import com.example.dashboard0x.data.remote.api.DashboardApi
import com.example.dashboard0x.domain.util.Resource
import com.example.dashboard0x.domain.util.Validators
import com.google.gson.Gson
import retrofit2.Response

class TodoRepository(
    private val api: DashboardApi,
    private val tokenManager: TokenManager
) {

    suspend fun getTodos(): Resource<TodoListData> {
        val token = tokenManager.getTokenWithBearer() ?: return Resource.Error("Not authenticated")
        return try {
            val response = api.getTodos(token)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                handleErrorResponse(response)
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun createTodo(text: String): Resource<Todo> {
        // Client-side validation
        Validators.validateTodoText(text)?.let {
            return Resource.Error(it)
        }

        val token = tokenManager.getTokenWithBearer() ?: return Resource.Error("Not authenticated")
        return try {
            val response = api.createTodo(token, CreateTodoRequest(text))
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                handleErrorResponse(response)
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun updateTodo(id: String, text: String? = null, completed: Boolean? = null): Resource<Todo> {
        // Client-side validation if text is being updated
        text?.let { newText ->
            Validators.validateTodoText(newText)?.let {
                return Resource.Error(it)
            }
        }

        val token = tokenManager.getTokenWithBearer() ?: return Resource.Error("Not authenticated")
        return try {
            val response = api.updateTodo(token, UpdateTodoRequest(id, text, completed))
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                handleErrorResponse(response)
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun deleteTodo(id: String): Resource<DeleteResponse> {
        val token = tokenManager.getTokenWithBearer() ?: return Resource.Error("Not authenticated")
        return try {
            val response = api.deleteTodo(token, DeleteTodoRequest(id))
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                handleErrorResponse(response)
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    private fun <T> handleErrorResponse(response: Response<T>): Resource<T> {
        val errorMessage = try {
            response.errorBody()?.string()?.let { errorBody ->
                Gson().fromJson(errorBody, ErrorResponse::class.java).error
            }
        } catch (e: Exception) {
            null
        }

        val message = errorMessage ?: when (response.code()) {
            401 -> "Session expired. Please login again."
            404 -> "Resource not found"
            400 -> "Invalid request"
            500 -> "Server error. Please try again later."
            else -> "Error: ${response.code()}"
        }
        return Resource.Error(message)
    }
}
