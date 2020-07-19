package com.example.github_app.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github_app.R
import com.example.github_app.model.Error
import com.example.github_app.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject

class SearchUserViewModel(
    private val mInteractor: SearchUserInteractor
) : BaseViewModel<SearchUserIntent, SearchUserViewState>() {

    private val mState: MutableLiveData<SearchUserViewState> = MutableLiveData()

    override fun bindIntent(intent: Observable<SearchUserIntent>) {
        addDisposable(
            intent
                .compose(mInteractor.compose())
                .scan(SearchUserViewState.default(), mReducer)
                .subscribe(
                    { mState.postValue(it) },
                    { Timber.e(it) }
                )
        )
    }

    override fun state(): MutableLiveData<SearchUserViewState> = mState

    private val mReducer =
        BiFunction { state: SearchUserViewState, changes: SearchUserPartialState ->
            when (changes) {
                is SearchUserPartialState.Search.InFlight -> {
                    SearchUserViewState.default().copy(
                        isLoading = true,
                        data = SearchUserData()
                    )
                }

                is SearchUserPartialState.Search.Success -> {
                    SearchUserViewState.default().copy(
                        data = state.data?.copy(
                            totalItemCount = changes.result.totalCount,
                            page = state.data.page + 1,
                            users = changes.result.users
                        )
                    )
                }

                is SearchUserPartialState.Search.Error -> {
                    SearchUserViewState.default().copy(
                        data = state.data?.copy(
                            initialError = Error(
                                image = R.drawable.ic_error,
                                message = changes.error
                            )
                        )
                    )
                }

                is SearchUserPartialState.LoadMore.InFlight -> {
                    SearchUserViewState.default().copy(
                        isLoadMoreLoading = true,
                        data = state.data
                    )
                }

                is SearchUserPartialState.LoadMore.Success -> {
                    val users = state.data?.users?.toMutableList() ?: mutableListOf()

                    changes.result.users.forEach { user ->
                        if (!users.contains(user)) {
                            users.add(user)
                        }
                    }

                    SearchUserViewState.default().copy(
                        data = state.data?.copy(
                            page = state.data.page + 1,
                            users = users
                        )
                    )
                }

                is SearchUserPartialState.LoadMore.Error -> {
                    SearchUserViewState.default().copy(
                        data = state.data?.copy(
                            loadMoreError = changes.error
                        )
                    )
                }
            }
        }
}

class SearchUserViewModelFactory @Inject constructor(
    private val mInteractor: SearchUserInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SearchUserInteractor::class.java)
            .newInstance(mInteractor)
    }
}