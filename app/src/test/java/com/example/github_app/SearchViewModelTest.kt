package com.example.github_app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.github_app.model.Error
import com.example.github_app.model.SearchUserResult
import com.example.github_app.model.User
import com.example.github_app.ui.search.*
import com.jraska.livedata.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mInteractor: SearchUserInteractor = mockk()
    private val mViewModel = SearchUserViewModel(mInteractor)

    private val mStateObserver: Observer<SearchUserViewState> = mockk(relaxed = true)

    @Before
    fun setup() {
        mViewModel.state().observeForever(mStateObserver)
    }

    @Test
    fun `bindIntent() for search user - should return list of users`() {
        val query = "bonioctavianus"
        val perPage = 20
        val page = 1

        val result = SearchUserResult(
            totalCount = 1,
            users = listOf(
                User(
                    id = 1,
                    username = "bonioctavianus",
                    avatar = "http://example.com/avatar.png"
                )
            )
        )

        val intent: Observable<SearchUserIntent> = Observable.just(
            SearchUserIntent.SearchUser(query, perPage, page)
        )

        val composer: ObservableTransformer<SearchUserIntent, SearchUserPartialState> =
            ObservableTransformer {
                Observable.just(
                    SearchUserPartialState.Search.InFlight,
                    SearchUserPartialState.Search.Success(result)
                )
            }

        every { mInteractor.compose() } returns composer

        mViewModel.bindIntent(intent)

        verify {
            mInteractor.compose()
        }

        mViewModel.state().test()
            .assertValue { it.data!!.totalItemCount == 1 }
            .assertValue { it.data!!.page == 1 }
            .assertValue { it.data!!.users!![0].id == 1 }
            .assertValue { it.data!!.users!![0].username == "bonioctavianus" }
            .assertValue { it.data!!.users!![0].avatar == "http://example.com/avatar.png" }
            .assertValue { it.data!!.initialError == null }
    }

    @Test
    fun `bindIntent() for search user when throwing exception - should return error`() {
        val query = "bonioctavianus"
        val perPage = 20
        val page = 1

        val error = Error(
            image = R.drawable.ic_error,
            message = "Booom!"
        )

        val intent: Observable<SearchUserIntent> = Observable.just(
            SearchUserIntent.SearchUser(query, perPage, page)
        )

        val composer: ObservableTransformer<SearchUserIntent, SearchUserPartialState> =
            ObservableTransformer {
                Observable.just(
                    SearchUserPartialState.Search.InFlight,
                    SearchUserPartialState.Search.Error(error = "Booom!")
                )
            }

        every { mInteractor.compose() } returns composer

        mViewModel.bindIntent(intent)

        verify {
            mInteractor.compose()
        }

        mViewModel.state().test()
            .assertValue { it.data!!.totalItemCount == 0 }
            .assertValue { it.data!!.page == 0 }
            .assertValue { it.data!!.users.isNullOrEmpty() }
            .assertValue { it.data!!.initialError == error }
    }

    @Test
    fun `bindIntent() for load more user - should return list of users`() {
        val query = "bonioctavianus"
        val perPage = 20
        val page = 2

        val result = SearchUserResult(
            totalCount = 10,
            users = listOf(
                User(
                    id = 2,
                    username = "bonioctavianus 2",
                    avatar = "http://example.com/avatar.png"
                )
            )
        )

        val intent: Observable<SearchUserIntent> = Observable.just(
            SearchUserIntent.LoadMoreUser(query, perPage, page)
        )

        val composer: ObservableTransformer<SearchUserIntent, SearchUserPartialState> =
            ObservableTransformer {
                Observable.just(
                    SearchUserPartialState.LoadMore.InFlight,
                    SearchUserPartialState.LoadMore.Success(result)
                )
            }

        mockkObject(SearchUserViewState)

        every { SearchUserViewState.default() } returns
                SearchUserViewState(
                    isLoading = false,
                    isLoadMoreLoading = false,
                    data = SearchUserData(
                        totalItemCount = 10,
                        page = 2,
                        users = listOf(
                            User(
                                id = 1,
                                username = "bonioctavianus",
                                avatar = "http://example.com/avatar.png"
                            )
                        )
                    )
                )

        every { mInteractor.compose() } returns composer

        mViewModel.bindIntent(intent)

        verify {
            mInteractor.compose()
        }

        mViewModel.state().test()
            .assertValue { it.data!!.page == 3 }
            .assertValue { it.data!!.users!![0].id == 1 }
            .assertValue { it.data!!.users!![0].username == "bonioctavianus" }
            .assertValue { it.data!!.users!![0].avatar == "http://example.com/avatar.png" }
            .assertValue { it.data!!.users!![1].id == 2 }
            .assertValue { it.data!!.users!![1].username == "bonioctavianus 2" }
            .assertValue { it.data!!.users!![1].avatar == "http://example.com/avatar.png" }
            .assertValue { it.data!!.loadMoreError == null }
    }

    @Test
    fun `bindIntent() for load more user when throwing exception - should return error`() {
        val query = "bonioctavianus"
        val perPage = 20
        val page = 2

        val intent: Observable<SearchUserIntent> = Observable.just(
            SearchUserIntent.LoadMoreUser(query, perPage, page)
        )

        val composer: ObservableTransformer<SearchUserIntent, SearchUserPartialState> =
            ObservableTransformer {
                Observable.just(
                    SearchUserPartialState.LoadMore.InFlight,
                    SearchUserPartialState.LoadMore.Error(error = "Booom!")
                )
            }

        mockkObject(SearchUserViewState)

        every { SearchUserViewState.default() } returns
                SearchUserViewState(
                    isLoading = false,
                    isLoadMoreLoading = false,
                    data = SearchUserData(
                        totalItemCount = 10,
                        page = 2,
                        users = listOf(
                            User(
                                id = 1,
                                username = "bonioctavianus",
                                avatar = "http://example.com/avatar.png"
                            )
                        )
                    )
                )

        every { mInteractor.compose() } returns composer

        mViewModel.bindIntent(intent)

        verify {
            mInteractor.compose()
        }

        mViewModel.state().test()
            .assertValue { it.data!!.page == 2 }
            .assertValue { it.data!!.users!!.size == 1 }
            .assertValue { it.data!!.users!![0].id == 1 }
            .assertValue { it.data!!.users!![0].username == "bonioctavianus" }
            .assertValue { it.data!!.users!![0].avatar == "http://example.com/avatar.png" }
            .assertValue { it.data!!.loadMoreError == "Booom!" }
    }
}