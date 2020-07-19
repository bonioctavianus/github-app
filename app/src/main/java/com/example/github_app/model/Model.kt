package com.example.github_app.model

interface Cell

data class SearchUserResult(
    val totalCount: Int,
    val users: List<User>
)

data class User(
    val id: Int,
    val username: String,
    val avatar: String
) : Cell

object Progress : Cell