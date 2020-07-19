package com.example.github_app.repository

import com.example.github_app.model.SearchUserResult
import com.example.github_app.model.User
import javax.inject.Inject

class GithubTransformer @Inject constructor() {

    fun transform(response: SearchUserResponse): SearchUserResult {
        return SearchUserResult(
            totalCount = response.totalCount ?: 0,
            users = response.users?.map { user ->
                User(
                    id = user.id ?: -1,
                    username = user.username ?: "",
                    avatar = user.avatar ?: ""
                )
            } ?: emptyList()
        )
    }
}