package com.example.dashboard0x.data.repository

import com.example.dashboard0x.data.local.TokenManager
import com.example.dashboard0x.data.model.auth.ErrorResponse
import com.example.dashboard0x.data.model.dashboard.CryptoData
import com.example.dashboard0x.data.model.dashboard.PublicDashboardData
import com.example.dashboard0x.data.remote.api.DashboardApi
import com.example.dashboard0x.domain.util.Resource
import com.google.gson.Gson
import retrofit2.Response

class DashboardRepository(
    private val api: DashboardApi,
    private val tokenManager: TokenManager
) {

    suspend fun getPrivateData(): Resource<CryptoData> {
        val token = tokenManager.getTokenWithBearer() ?: return Resource.Error("Not authenticated")
        return try {
            val response = api.getPrivateData(token)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                handleErrorResponse(response)
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun getPublicData(): Resource<PublicDashboardData> {
        return try {
            val response = api.getPublicData()
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
