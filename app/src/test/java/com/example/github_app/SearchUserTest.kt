package com.example.github_app

import com.example.github_app.model.SearchUserResult
import com.example.github_app.network.SchedulersPool
import com.example.github_app.repository.GithubRepository
import com.example.github_app.usecase.Result
import com.example.github_app.usecase.SearchUser
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class SearchUserTest {

    private val mRepository: GithubRepository = mockk()
    private val mSchedulers: SchedulersPool = mockk()
    private val mSearchUser = SearchUser(mRepository, mSchedulers)

    @Before
    fun setup() {
        every { mSchedulers.io() } returns Schedulers.trampoline()
        every { mSchedulers.ui() } returns Schedulers.trampoline()
    }

    @Test
    fun `searchUsers() - should return list of users`() {
        val query = "bonioctavianus"
        val perPage = 20
        val page = 1
        val result = SearchUserResult(
            totalCount = 0,
            users = emptyList()
        )

        every { mRepository.searchUsers(query, perPage, page) } returns
                Single.just(result)

        mSearchUser.searchUsers(query, perPage, page)
            .test()
            .assertValues(
                Result.InFlight,
                Result.Success(result)
            )
            .dispose()
    }

    @Test
    fun `searchUsers() when throwing exception - should return error`() {
        val query = "bonioctavianus"
        val perPage = 20
        val page = 1
        val exception = RuntimeException("Booooom!")

        every { mRepository.searchUsers(query, perPage, page) } returns
                Single.error(exception)

        mSearchUser.searchUsers(query, perPage, page)
            .test()
            .assertValues(
                Result.InFlight,
                Result.Error(exception)
            )
            .dispose()
    }
}