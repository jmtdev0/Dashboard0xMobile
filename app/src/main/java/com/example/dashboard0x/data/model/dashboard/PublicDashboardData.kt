package com.example.dashboard0x.data.model.dashboard

data class PublicDashboardData(
    val results: PublicResults,
    val timestamp: Long
)

data class PublicResults(
    val youtube: YoutubeData?,
    val twitter: TwitterData?,
    val instagram: InstagramData?,
    val github: GithubData?,
    val extensions: List<ExtensionData>?
)

data class YoutubeData(
    val subscribers: String?,
    val views: String?,
    val videos: Int?,
    val success: Boolean
)

data class TwitterData(
    val followers: String?,
    val tweets: String?,
    val success: Boolean
)

data class InstagramData(
    val followers: String?,
    val posts: Int?,
    val success: Boolean
)

data class GithubData(
    val repos: Int?,
    val stars: Int?,
    val followers: Int?,
    val success: Boolean
)

data class ExtensionData(
    val name: String,
    val users: String?,
    val rating: String?,
    val success: Boolean
)
