package com.example.github_app

import com.example.github_app.repository.GithubApi
import com.example.github_app.repository.GithubService
import com.example.github_app.repository.GithubServiceV1
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class GithubServiceTest {

    private val mApi: GithubApi = mockk(relaxed = true)
    private val mService: GithubService = GithubServiceV1(mApi)

    @Test
    fun `searchUser() should create search API call`() {
        val query = "bonioctavianus"
        val perPage = 20
        val page = 1

        mService.searchUsers(query, perPage, page)

        verify {
            mApi.searchUsers(query, perPage, page)
        }
    }
}