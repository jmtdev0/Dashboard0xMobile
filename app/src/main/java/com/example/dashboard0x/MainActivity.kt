package com.example.dashboard0x

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.dashboard0x.data.local.TokenManager
import com.example.dashboard0x.data.remote.RetrofitClient
import com.example.dashboard0x.data.repository.AuthRepository
import com.example.dashboard0x.data.repository.DashboardRepository
import com.example.dashboard0x.data.repository.TodoRepository
import com.example.dashboard0x.presentation.navigation.NavGraph
import com.example.dashboard0x.presentation.navigation.Screen
import com.example.dashboard0x.ui.theme.Dashboard0xTheme

class MainActivity : ComponentActivity() {
    private lateinit var tokenManager: TokenManager
    private lateinit var authRepository: AuthRepository
    private lateinit var todoRepository: TodoRepository
    private lateinit var dashboardRepository: DashboardRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize dependencies
        tokenManager = TokenManager(applicationContext)
        val api = RetrofitClient.api
        authRepository = AuthRepository(api, tokenManager)
        todoRepository = TodoRepository(api, tokenManager)
        dashboardRepository = DashboardRepository(api, tokenManager)

        setContent {
            Dashboard0xTheme {
                val navController = rememberNavController()
                var startDestination by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(Unit) {
                    startDestination = if (authRepository.isAuthenticated()) {
                        Screen.TodoList.route
                    } else {
                        Screen.Login.route
                    }
                }

                startDestination?.let { destination ->
                    Surface(modifier = Modifier.fillMaxSize()) {
                        NavGraph(
                            navController = navController,
                            startDestination = destination,
                            authRepository = authRepository,
                            todoRepository = todoRepository,
                            dashboardRepository = dashboardRepository
                        )
                    }
                }
            }
        }
    }
}
