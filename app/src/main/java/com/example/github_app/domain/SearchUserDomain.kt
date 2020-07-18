package com.example.github_app.domain

data class SearchUserResult(
    val totalCount: Int,
    val users: List<User>
)

data class User(
    val username: String,
    val avatar: String
)