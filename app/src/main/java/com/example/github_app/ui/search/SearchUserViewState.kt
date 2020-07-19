package com.example.github_app.ui.search

import com.example.github_app.R
import com.example.github_app.model.Cell
import com.example.github_app.model.SearchUserResult
import com.example.github_app.ui.base.MviViewState

data class SearchUserViewState(
    val isTotalItemLabelVisible: Boolean,
    val data: SearchUserData?,
    val errorImage: Int,
    val initialError: String?,
    val loadMoreError: String?
) : MviViewState {

    companion object {
        fun default(): SearchUserViewState {
            return SearchUserViewState(
                isTotalItemLabelVisible = true,
                data = null,
                errorImage = R.drawable.ic_error,
                initialError = null,
                loadMoreError = null
            )
        }
    }
}

data class SearchUserData(
    val totalItemCount: Int,
    val page: Int,
    val cells: List<Cell>?,
    val emptyImage: Int = R.drawable.ic_search,
    val emptyMessage: Int = R.string.search_user_result_not_found
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