package com.example.github_app.ui.base

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable

interface MviViewModel<I : MviIntent, S : MviViewState> {
    fun bindIntent(intent: Observable<I>)
    fun state(): MutableLiveData<S>
}