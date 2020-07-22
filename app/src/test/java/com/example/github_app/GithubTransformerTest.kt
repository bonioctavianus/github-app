package com.example.github_app

import com.example.github_app.repository.GithubTransformer
import com.example.github_app.repository.SearchUserResponse
import com.example.github_app.repository.UserResponse
import org.junit.Test

class GithubTransformerTest {

    private val mTransformer = GithubTransformer()

    @Test
    fun `transform() - should return correct data`() {
        val response = SearchUserResponse(
            totalCount = 1,
            users = listOf(
                UserResponse(
                    id = 1,
                    username = "bonioctavianus",
                    avatar = "http://example.com/avatar.png"
                )
            )
        )

        val result = mTransformer.transform(response)

        assert(result.totalCount == 1)
        assert(result.users[0].id == 1)
        assert(result.users[0].username == "bonioctavianus")
        assert(result.users[0].avatar == "http://example.com/avatar.png")
    }
}