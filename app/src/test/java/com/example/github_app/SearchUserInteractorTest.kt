package com.example.github_app

import com.example.github_app.model.SearchUserResult
import com.example.github_app.ui.search.SearchUserIntent
import com.example.github_app.ui.search.SearchUserInteractor
import com.example.github_app.ui.search.SearchUserPartialState
import com.example.github_app.usecase.Result
import com.example.github_app.usecase.SearchUser
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.junit.Test

class SearchUserInteractorTest {

    private val mSearchUser: SearchUser = mockk()
    private val mInteractor = SearchUserInteractor(mSearchUser)

    @Test
    fun `compose() for search user intent - should return success state`() {
        val query = "bonioctavianus"
        val perPage = 20
        val page = 1
        val result = SearchUserResult(
            totalCount = 0,
            users = emptyList()
        )

        every { mSearchUser.searchUsers(query, perPage, page) } returns
                Observable.just(
                    Result.InFlight,
                    Result.Success(result)
                )

        Observable.just(
            SearchUserIntent.SearchUser(query, perPage, page)
        )
            .compose(mInteractor.compose())
            .test()
            .assertValues(
                SearchUserPartialState.Search.InFlight,
                SearchUserPartialState.Search.Success(result)
            )
            .dispose()
    }

    @Test
    fun `compose() for search user intent when throwing exception - should return error state`() {
        val query = "bonioctavianus"
        val perPage = 20
        val page = 1
        val exception = RuntimeException("Booooom!")

        every { mSearchUser.searchUsers(query, perPage, page) } returns
                Observable.just(
                    Result.InFlight,
                    Result.Error(exception)
                )

        Observable.just(
            SearchUserIntent.SearchUser(query, perPage, page)
        )
            .compose(mInteractor.compose())
            .test()
            .assertValues(
                SearchUserPartialState.Search.InFlight,
                SearchUserPartialState.Search.Error("Booooom!")
            )
            .dispose()
    }

    @Test
    fun `compose() for load more user intent - should return success state`() {
        val query = "bonioctavianus"
        val perPage = 20
        val page = 1
        val result = SearchUserResult(
            totalCount = 0,
            users = emptyList()
        )

        every { mSearchUser.searchUsers(query, perPage, page) } returns
                Observable.just(
                    Result.InFlight,
                    Result.Success(result)
                )

        Observable.just(
            SearchUserIntent.LoadMoreUser(query, perPage, page)
        )
            .compose(mInteractor.compose())
            .test()
            .assertValues(
                SearchUserPartialState.LoadMore.InFlight,
                SearchUserPartialState.LoadMore.Success(result)
            )
            .dispose()
    }

    @Test
    fun `compose() for load more user intent when throwing exception - should return error state`() {
        val query = "bonioctavianus"
        val perPage = 20
        val page = 1
        val exception = RuntimeException("Booooom!")

        every { mSearchUser.searchUsers(query, perPage, page) } returns
                Observable.just(
                    Result.InFlight,
                    Result.Error(exception)
                )

        Observable.just(
            SearchUserIntent.LoadMoreUser(query, perPage, page)
        )
            .compose(mInteractor.compose())
            .test()
            .assertValues(
                SearchUserPartialState.LoadMore.InFlight,
                SearchUserPartialState.LoadMore.Error("Booooom!")
            )
            .dispose()
    }
}