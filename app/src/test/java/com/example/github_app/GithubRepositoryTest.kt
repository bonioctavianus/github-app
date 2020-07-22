package com.example.github_app

import com.example.github_app.repository.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test

class GithubRepositoryTest {

    private val mService: GithubService = mockk()
    private val mTransformer: GithubTransformer = mockk(relaxed = true)
    private val mRepository: GithubRepository = GithubRepositoryV1(mService, mTransformer)

    @Test
    fun `searchUsers() - should call service and transformer`() {
        val query = "bonioctavianus"
        val perPage = 20
        val page = 1
        val response = SearchUserResponse(
            totalCount = 0,
            users = emptyList()
        )

        every { mService.searchUsers(query, perPage, page) } returns
                Single.just(response)

        mRepository.searchUsers(query, perPage, page).blockingGet()

        verify {
            mService.searchUsers(query, perPage, page)
            mTransformer.transform(response)
        }
    }
}