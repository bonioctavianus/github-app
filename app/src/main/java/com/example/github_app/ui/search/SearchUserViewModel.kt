package com.example.github_app.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github_app.model.Progress
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
                    val cells = listOf(Progress)

                    SearchUserViewState.default().copy(
                        isTotalItemLabelVisible = false,
                        data = SearchUserData(
                            totalItemCount = 0,
                            page = 0,
                            cells = cells
                        )
                    )
                }

                is SearchUserPartialState.Search.Success -> {
                    SearchUserViewState.default().copy(
                        data = state.data?.copy(
                            totalItemCount = changes.result.totalCount,
                            page = state.data.page + 1,
                            cells = changes.result.users
                        )
                    )
                }

                is SearchUserPartialState.Search.Error -> {
                    SearchUserViewState.default().copy(
                        isTotalItemLabelVisible = false,
                        initialError = changes.error
                    )
                }

                is SearchUserPartialState.LoadMore.InFlight -> {
                    val cells = state.data?.cells?.toMutableList()
                    cells?.add(Progress)

                    SearchUserViewState.default().copy(
                        data = state.data?.copy(
                            cells = cells
                        )
                    )
                }

                is SearchUserPartialState.LoadMore.Success -> {
                    val cells = state.data?.cells?.toMutableList() ?: mutableListOf()
                    cells.remove(Progress)

                    changes.result.users.forEach { user ->
                        if (!cells.contains(user)) {
                            cells.add(user)
                        }
                    }

                    SearchUserViewState.default().copy(
                        data = state.data?.copy(
                            page = state.data.page + 1,
                            cells = cells
                        )
                    )
                }

                is SearchUserPartialState.LoadMore.Error -> {
                    val cells = state.data?.cells?.toMutableList()
                    cells?.remove(Progress)

                    SearchUserViewState.default().copy(
                        data = state.data?.copy(
                            cells = cells
                        ),
                        loadMoreError = changes.error
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