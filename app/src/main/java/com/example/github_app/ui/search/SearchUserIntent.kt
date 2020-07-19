package com.example.github_app.ui.search

import com.example.github_app.ui.base.MviIntent

sealed class SearchUserIntent : MviIntent {

    data class SearchUser(
        val query: String,
        val perPage: Int,
        val page: Int
    ) : SearchUserIntent()

    data class LoadMoreUser(
        val query: String,
        val perPage: Int,
        val page: Int
    ) : SearchUserIntent()
}