package com.example.github_app.repository

import com.example.github_app.domain.SearchUserResult
import com.example.github_app.domain.User
import javax.inject.Inject

class GithubTransformer @Inject constructor() {

    fun transform(response: SearchUserResponse): SearchUserResult {
        return SearchUserResult(
            totalCount = response.totalCount ?: 0,
            users = response.users?.map { user ->
                User(
                    username = user.username ?: "",
                    avatar = user.avatar ?: ""
                )
            } ?: emptyList()
        )
    }
}