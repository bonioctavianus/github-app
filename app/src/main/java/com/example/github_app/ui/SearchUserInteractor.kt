package com.example.github_app.ui

import com.example.github_app.domain.SearchUserResult
import com.example.github_app.usecase.Result
import com.example.github_app.usecase.SearchUser
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class SearchUserInteractor @Inject constructor(
    private val mSearchUser: SearchUser
) {
    fun compose(): ObservableTransformer<SearchUserIntent, SearchUserViewState> {
        return ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.mergeArray(
                    shared.ofType(SearchUserIntent.SearchUser::class.java)
                        .compose(searchUser)
                ).mergeWith(
                    shared.filter { intent ->
                        intent !is SearchUserIntent.SearchUser
                    }.flatMap { intent ->
                        Observable.error<SearchUserViewState>(
                            IllegalStateException("Unknown intent type: $intent")
                        )
                    }
                )
            }
        }
    }

    private val searchUser =
        ObservableTransformer<SearchUserIntent.SearchUser, SearchUserViewState> { intents ->
            intents.flatMap { intent ->
                mSearchUser.searchUsers(
                    intent.query,
                    intent.perPage,
                    intent.page
                )
                    .map { result ->
                        when (result) {
                            is Result.InFlight -> SearchUserViewState.InFlight
                            is Result.Success<*> -> SearchUserViewState.Success(result.item as SearchUserResult)
                            is Result.Error -> SearchUserViewState.Error(result.throwable)
                        }
                    }
            }
        }
}