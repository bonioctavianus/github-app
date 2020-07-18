package com.example.github_app.repository

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    @SerializedName(value = "total_count") val totalCount: Int?,
    @SerializedName(value = "items") val users: List<UserResponse>?
)

data class UserResponse(
    @SerializedName(value = "login") val username: String?,
    @SerializedName(value = "avatar_url") val avatar: String?
)