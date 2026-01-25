package com.example.dashboard0x.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dashboard0x.data.repository.AuthRepository
import com.example.dashboard0x.data.repository.DashboardRepository
import com.example.dashboard0x.data.repository.TodoRepository
import com.example.dashboard0x.presentation.auth.LoginScreen
import com.example.dashboard0x.presentation.auth.LoginViewModel
import com.example.dashboard0x.presentation.dashboard.PrivateDashboardScreen
import com.example.dashboard0x.presentation.dashboard.PrivateDashboardViewModel
import com.example.dashboard0x.presentation.dashboard.PublicDashboardScreen
import com.example.dashboard0x.presentation.dashboard.PublicDashboardViewModel
import com.example.dashboard0x.presentation.todo.TodoListScreen
import com.example.dashboard0x.presentation.todo.TodoViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    authRepository: AuthRepository,
    todoRepository: TodoRepository,
    dashboardRepository: DashboardRepository
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            val viewModel = remember { LoginViewModel(authRepository) }
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.TodoList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.TodoList.route) {
            val viewModel = remember { TodoViewModel(todoRepository) }
            TodoListScreen(
                viewModel = viewModel,
                onNavigateToPrivateDashboard = {
                    navController.navigate(Screen.PrivateDashboard.route)
                },
                onNavigateToPublicDashboard = {
                    navController.navigate(Screen.PublicDashboard.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.PrivateDashboard.route) {
            val viewModel = remember { PrivateDashboardViewModel(dashboardRepository) }
            PrivateDashboardScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.PublicDashboard.route) {
            val viewModel = remember { PublicDashboardViewModel(dashboardRepository) }
            PublicDashboardScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
