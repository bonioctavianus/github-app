package com.example.github_app.ui

import com.example.github_app.domain.SearchUserResult
import com.example.github_app.ui.base.MviViewState

sealed class SearchUserViewState : MviViewState {
    object InFlight : SearchUserViewState()
    data class Success(val result: SearchUserResult) : SearchUserViewState()
    data class Error(val throwable: Throwable?) : SearchUserViewState()
}