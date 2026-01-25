package com.example.dashboard0x.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TokenManager(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val EXPIRES_AT_KEY = longPreferencesKey("token_expires_at")
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("auth_prefs")
    }

    suspend fun saveToken(token: String, expiresAt: Long) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[EXPIRES_AT_KEY] = expiresAt
        }
    }

    suspend fun getToken(): String? {
        val prefs = context.dataStore.data.first()
        val token = prefs[TOKEN_KEY]
        val expiresAt = prefs[EXPIRES_AT_KEY] ?: return null

        if (System.currentTimeMillis() >= expiresAt) {
            clearToken()
            return null
        }
        return token
    }

    suspend fun getTokenWithBearer(): String? = getToken()?.let { "Bearer $it" }

    suspend fun clearToken() {
        context.dataStore.edit { it.clear() }
    }

    fun isTokenValid(): Flow<Boolean> = context.dataStore.data.map { prefs ->
        val expiresAt = prefs[EXPIRES_AT_KEY] ?: return@map false
        System.currentTimeMillis() < expiresAt
    }
}
