package com.example.github_app.ui.base

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable

interface MviView<I : MviIntent, S : MviViewState> {
    fun intents(): Observable<I>
    fun bindIntent(intent: Observable<I>)
    fun observeState(state: MutableLiveData<S>)
    fun state(): MutableLiveData<S>
}