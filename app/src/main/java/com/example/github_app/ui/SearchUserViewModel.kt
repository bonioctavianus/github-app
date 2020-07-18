package com.example.github_app.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github_app.ui.base.BaseViewModel
import io.reactivex.Observable
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
                .subscribe(
                    { mState.postValue(it) },
                    { Timber.e(it) }
                )
        )
    }

    override fun state(): MutableLiveData<SearchUserViewState> = mState
}

class SearchUserViewModelFactory @Inject constructor(
    private val mInteractor: SearchUserInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SearchUserInteractor::class.java)
            .newInstance(mInteractor)
    }
}