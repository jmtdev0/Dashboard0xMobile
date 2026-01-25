package com.example.dashboard0x.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object TodoList : Screen("todo_list")
    object PrivateDashboard : Screen("private_dashboard")
    object PublicDashboard : Screen("public_dashboard")
}
