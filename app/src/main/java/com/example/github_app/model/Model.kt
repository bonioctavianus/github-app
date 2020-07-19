package com.example.github_app.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SearchUserResult(
    val totalCount: Int,
    val users: List<User>
)

data class User(
    val id: Int,
    val username: String,
    val avatar: String
)

data class Info(
    @DrawableRes val image: Int,
    @StringRes val message: Int
)

data class Error(
    @DrawableRes val image: Int,
    val message: String
)