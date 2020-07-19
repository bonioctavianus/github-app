package com.example.github_app.ui.search

import com.example.github_app.model.SearchUserResult
import com.example.github_app.usecase.Result
import com.example.github_app.usecase.SearchUser
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class SearchUserInteractor @Inject constructor(
    private val mSearchUser: SearchUser
) {
    fun compose(): ObservableTransformer<SearchUserIntent, SearchUserPartialState> {
        return ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.mergeArray(
                    shared.ofType(SearchUserIntent.SearchUser::class.java)
                        .compose(searchUser),
                    shared.ofType(SearchUserIntent.LoadMoreUser::class.java)
                        .compose(loadMoreUser)
                ).mergeWith(
                    shared.filter { intent ->
                        intent !is SearchUserIntent.SearchUser
                                && intent !is SearchUserIntent.LoadMoreUser
                    }.flatMap { intent ->
                        Observable.error<SearchUserPartialState>(
                            IllegalStateException("Unknown intent type: $intent")
                        )
                    }
                )
            }
        }
    }

    private val searchUser =
        ObservableTransformer<SearchUserIntent.SearchUser, SearchUserPartialState> { intents ->
            intents.flatMap { intent ->
                mSearchUser.searchUsers(
                    intent.query,
                    intent.perPage,
                    intent.page
                ).map(::mapSearchResult)
            }
        }

    private fun mapSearchResult(result: Result): SearchUserPartialState {
        return when (result) {
            is Result.InFlight -> {
                SearchUserPartialState.Search.InFlight
            }

            is Result.Success<*> -> {
                SearchUserPartialState.Search.Success(
                    result.item as SearchUserResult
                )
            }

            is Result.Error -> {
                SearchUserPartialState.Search.Error(
                    result.throwable?.message ?: "Unknown error"
                )
            }
        }
    }

    private val loadMoreUser =
        ObservableTransformer<SearchUserIntent.LoadMoreUser, SearchUserPartialState> { intents ->
            intents.flatMap { intent ->
                mSearchUser.searchUsers(
                    intent.query,
                    intent.perPage,
                    intent.page
                ).map(::mapLoadMoreSearchResult)
            }
        }

    private fun mapLoadMoreSearchResult(result: Result): SearchUserPartialState {
        return when (result) {
            is Result.InFlight -> {
                SearchUserPartialState.LoadMore.InFlight
            }

            is Result.Success<*> -> {
                SearchUserPartialState.LoadMore.Success(
                    result.item as SearchUserResult
                )
            }

            is Result.Error -> {
                SearchUserPartialState.LoadMore.Error(
                    result.throwable?.message ?: "Unknown error"
                )
            }
        }
    }
}