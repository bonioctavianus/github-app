package com.example.github_app.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.github_app.R
import com.example.github_app.ui.base.BaseFragment
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import javax.inject.Inject

class SearchUserFragment : BaseFragment<SearchUserIntent, SearchUserViewState>() {

    @Inject
    lateinit var mViewModel: SearchUserViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_user, container, false)
    }

    override fun intents(): Observable<SearchUserIntent> {
        return Observable.just(
            SearchUserIntent.SearchUser(
                query = "bonioctavianus",
                perPage = 10,
                page = 1
            )
        )
    }

    override fun bindIntent(intent: Observable<SearchUserIntent>) {
        mViewModel.bindIntent(intent)
    }

    override fun observeState(state: MutableLiveData<SearchUserViewState>) {
        state.observe(viewLifecycleOwner, Observer { value ->
            when (value) {
                is SearchUserViewState.Success -> {
                    Toast.makeText(
                        requireContext(),
                        value.result.users.first().username,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    override fun state(): MutableLiveData<SearchUserViewState> = mViewModel.state()
}