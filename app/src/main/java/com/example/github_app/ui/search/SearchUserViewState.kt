package com.example.github_app.ui.search

import com.example.github_app.R
import com.example.github_app.model.Error
import com.example.github_app.model.Info
import com.example.github_app.model.SearchUserResult
import com.example.github_app.model.User
import com.example.github_app.ui.base.MviViewState

data class SearchUserViewState(
    val isLoading: Boolean,
    val isLoadMoreLoading: Boolean,
    val data: SearchUserData?
) : MviViewState {

    companion object {
        fun default(): SearchUserViewState {
            return SearchUserViewState(
                isLoading = false,
                isLoadMoreLoading = false,
                data = null
            )
        }
    }
}

data class SearchUserData(
    val totalItemCount: Int = 0,
    val page: Int = 0,
    val users: List<User>? = null,
    val emptyInfo: Info = Info(
        R.drawable.ic_search,
        R.string.search_user_result_not_found
    ),
    val initialError: Error? = null,
    val loadMoreError: String? = null
)

sealed class SearchUserPartialState {

    sealed class Search : SearchUserPartialState() {
        object InFlight : SearchUserPartialState()
        data class Success(val result: SearchUserResult) : SearchUserPartialState()
        data class Error(val error: String) : SearchUserPartialState()
    }

    sealed class LoadMore : SearchUserPartialState() {
        object InFlight : SearchUserPartialState()
        data class Success(val result: SearchUserResult) : SearchUserPartialState()
        data class Error(val error: String) : SearchUserPartialState()
    }
}