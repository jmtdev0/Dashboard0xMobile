package com.example.dashboard0x.data.repository

import com.example.dashboard0x.data.local.TokenManager
import com.example.dashboard0x.data.model.auth.ErrorResponse
import com.example.dashboard0x.data.model.auth.LoginRequest
import com.example.dashboard0x.data.model.auth.LoginResponse
import com.example.dashboard0x.data.remote.api.DashboardApi
import com.example.dashboard0x.domain.util.Resource
import com.google.gson.Gson
import retrofit2.Response

class AuthRepository(
    private val api: DashboardApi,
    private val tokenManager: TokenManager
) {

    suspend fun login(password: String): Resource<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(password))
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                tokenManager.saveToken(loginResponse.token, loginResponse.expiresAt)
                Resource.Success(loginResponse)
            } else {
                handleErrorResponse(response)
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun logout() {
        tokenManager.clearToken()
    }

    suspend fun isAuthenticated(): Boolean {
        return tokenManager.getToken() != null
    }

    private fun <T> handleErrorResponse(response: Response<T>): Resource<T> {
        val errorMessage = try {
            response.errorBody()?.string()?.let { errorBody ->
                Gson().fromJson(errorBody, ErrorResponse::class.java).error
            } ?: "Unknown error"
        } catch (e: Exception) {
            when (response.code()) {
                401 -> "Invalid password"
                500 -> "Server error. Please try again later."
                else -> "Error: ${response.code()}"
            }
        }
        return Resource.Error(errorMessage)
    }
}
