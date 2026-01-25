package com.example.dashboard0x.data.remote.api

import com.example.dashboard0x.data.model.auth.LoginRequest
import com.example.dashboard0x.data.model.auth.LoginResponse
import com.example.dashboard0x.data.model.dashboard.CryptoData
import com.example.dashboard0x.data.model.dashboard.PublicDashboardData
import com.example.dashboard0x.data.model.todo.CreateTodoRequest
import com.example.dashboard0x.data.model.todo.DeleteResponse
import com.example.dashboard0x.data.model.todo.DeleteTodoRequest
import com.example.dashboard0x.data.model.todo.Todo
import com.example.dashboard0x.data.model.todo.TodoListData
import com.example.dashboard0x.data.model.todo.UpdateTodoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface DashboardApi {
    @POST("api/auth/verify")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/todos")
    suspend fun getTodos(@Header("Authorization") token: String): Response<TodoListData>

    @POST("api/todos")
    suspend fun createTodo(
        @Header("Authorization") token: String,
        @Body request: CreateTodoRequest
    ): Response<Todo>

    @PUT("api/todos")
    suspend fun updateTodo(
        @Header("Authorization") token: String,
        @Body request: UpdateTodoRequest
    ): Response<Todo>

    @HTTP(method = "DELETE", path = "api/todos", hasBody = true)
    suspend fun deleteTodo(
        @Header("Authorization") token: String,
        @Body request: DeleteTodoRequest
    ): Response<DeleteResponse>

    @GET("api/private/data")
    suspend fun getPrivateData(@Header("Authorization") token: String): Response<CryptoData>

    @GET("api/results")
    suspend fun getPublicData(): Response<PublicDashboardData>
}
