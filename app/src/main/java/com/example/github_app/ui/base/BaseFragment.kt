package com.example.github_app.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment<I : MviIntent, S : MviViewState> : Fragment(), MviView<I, S> {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindIntent(intents())
        observeState(state())
    }
}