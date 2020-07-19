package com.example.github_app.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.github_app.R
import com.example.github_app.ui.base.BaseFragment
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_search_user.*
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component_search_result.mActivity = requireActivity()
    }

    override fun intents(): Observable<SearchUserIntent> {
        return component_search_result.intents()
    }

    override fun bindIntent(intent: Observable<SearchUserIntent>) {
        mViewModel.bindIntent(intent)
    }

    override fun observeState(state: MutableLiveData<SearchUserViewState>) {
        state.observe(viewLifecycleOwner, Observer { value ->
            component_search_result.renderViewState(value)
        })
    }

    override fun state(): MutableLiveData<SearchUserViewState> = mViewModel.state()
}