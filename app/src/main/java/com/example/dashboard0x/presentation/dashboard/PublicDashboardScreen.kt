package com.example.dashboard0x.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dashboard0x.data.model.dashboard.ExtensionData
import com.example.dashboard0x.data.model.dashboard.GithubData
import com.example.dashboard0x.data.model.dashboard.InstagramData
import com.example.dashboard0x.data.model.dashboard.TwitterData
import com.example.dashboard0x.data.model.dashboard.YoutubeData
import com.example.dashboard0x.presentation.components.ErrorMessage
import com.example.dashboard0x.presentation.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicDashboardScreen(
    viewModel: PublicDashboardViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Public Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading && uiState.data == null -> {
                LoadingIndicator(modifier = Modifier.padding(paddingValues))
            }
            uiState.error != null && uiState.data == null -> {
                ErrorMessage(
                    message = uiState.error ?: "Unknown error",
                    onRetry = { viewModel.loadData() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            uiState.data != null -> {
                PullToRefreshBox(
                    isRefreshing = uiState.isLoading,
                    onRefresh = { viewModel.loadData() },
                    modifier = Modifier.padding(paddingValues)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        uiState.data!!.results.youtube?.let { youtube ->
                            YoutubeCard(youtube)
                        }

                        uiState.data!!.results.twitter?.let { twitter ->
                            TwitterCard(twitter)
                        }

                        uiState.data!!.results.instagram?.let { instagram ->
                            InstagramCard(instagram)
                        }

                        uiState.data!!.results.github?.let { github ->
                            GithubCard(github)
                        }

                        uiState.data!!.results.extensions?.let { extensions ->
                            if (extensions.isNotEmpty()) {
                                Text(
                                    text = "Extensions",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                extensions.forEach { extension ->
                                    ExtensionCard(extension)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun YoutubeCard(data: YoutubeData) {
    SocialCard(
        title = "YouTube",
        success = data.success,
        content = {
            if (data.success) {
                InfoRow("Subscribers", data.subscribers ?: "N/A")
                InfoRow("Views", data.views ?: "N/A")
                InfoRow("Videos", data.videos?.toString() ?: "N/A")
            }
        }
    )
}

@Composable
private fun TwitterCard(data: TwitterData) {
    SocialCard(
        title = "Twitter",
        success = data.success,
        content = {
            if (data.success) {
                InfoRow("Followers", data.followers ?: "N/A")
                InfoRow("Tweets", data.tweets ?: "N/A")
            }
        }
    )
}

@Composable
private fun InstagramCard(data: InstagramData) {
    SocialCard(
        title = "Instagram",
        success = data.success,
        content = {
            if (data.success) {
                InfoRow("Followers", data.followers ?: "N/A")
                InfoRow("Posts", data.posts?.toString() ?: "N/A")
            }
        }
    )
}

@Composable
private fun GithubCard(data: GithubData) {
    SocialCard(
        title = "GitHub",
        success = data.success,
        content = {
            if (data.success) {
                InfoRow("Repositories", data.repos?.toString() ?: "N/A")
                InfoRow("Stars", data.stars?.toString() ?: "N/A")
                InfoRow("Followers", data.followers?.toString() ?: "N/A")
            }
        }
    )
}

@Composable
private fun ExtensionCard(data: ExtensionData) {
    SocialCard(
        title = data.name,
        success = data.success,
        content = {
            if (data.success) {
                InfoRow("Users", data.users ?: "N/A")
                InfoRow("Rating", data.rating ?: "N/A")
            }
        }
    )
}

@Composable
private fun SocialCard(
    title: String,
    success: Boolean,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
                Icon(
                    imageVector = if (success) {
                        Icons.Default.CheckCircle
                    } else {
                        Icons.Default.Error
                    },
                    contentDescription = if (success) "Success" else "Failed",
                    tint = if (success) Color.Green else Color.Red
                )
            }
            if (success) {
                Spacer(modifier = Modifier.height(8.dp))
                content()
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Failed to fetch data",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
