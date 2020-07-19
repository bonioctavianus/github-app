package com.example.github_app.ui.search

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import com.example.github_app.R
import com.example.github_app.helper.gone
import com.example.github_app.helper.hideSoftKeyboard
import com.example.github_app.helper.visible
import com.example.github_app.model.Error
import io.reactivex.Observable
import kotlinx.android.synthetic.main.component_search_user.view.*

class SearchUserComponent(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {

    companion object {
        private const val SEARCH_USER_DEFAULT_PER_PAGE = 20
    }

    var mActivity: FragmentActivity? = null

    init {
        View.inflate(context, R.layout.component_search_user, this)

        initInputTextListener()
    }

    private fun initInputTextListener() {
        input_edit_search.addTextChangedListener {
            val text = it.toString()
            button_search.isEnabled = text.isNotBlank()
        }
    }

    fun renderViewState(state: SearchUserViewState) = with(state) {
        val initialError = data?.initialError
        val loadMoreError = data?.loadMoreError

        when {
            isLoading -> renderInitialLoading()
            isLoadMoreLoading -> renderLoadMoreLoading()
            initialError != null -> renderInitialError(initialError)
            loadMoreError != null -> renderLoadMoreError(loadMoreError)
            else -> data?.let { renderSearchResult(it) }
        }
    }

    private fun renderInitialLoading() {
        text_total_item_count.gone()

        list_search_result.apply {
            submitLoadingState(true)
            submitEmptyState(null)
            submitErrorState(null)
            submitUsers(
                totalItemCount = 0,
                page = 0,
                users = emptyList()
            )
        }
    }

    private fun renderLoadMoreLoading() {
        list_search_result.submitLoadingState(true)
    }

    private fun renderInitialError(error: Error?) {
        list_search_result.apply {
            submitLoadingState(false)
            submitErrorState(error)
        }
    }

    private fun renderLoadMoreError(message: String?) {
        list_search_result.submitLoadingState(false)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun renderSearchResult(data: SearchUserData) = with(data) {
        val users = users ?: return

        list_search_result.submitLoadingState(false)

        if (users.isNotEmpty()) {
            list_search_result.submitUsers(totalItemCount, page, users)

        } else {
            list_search_result.submitEmptyState(emptyInfo)
        }

        renderTotalItemCount(totalItemCount)
    }

    private fun renderTotalItemCount(totalItemCount: Int) {
        val totalItemText = context.getString(
            R.string.search_user_result_total_item_count,
            totalItemCount
        )

        text_total_item_count.text = totalItemText
        text_total_item_count.visible()
    }

    fun intents(): Observable<SearchUserIntent> {
        return Observable.merge(
            createSearchUserIntent(),
            createLoadMoreUserIntent()
        ).doOnNext { hideSoftKeyboard(mActivity) }
    }

    private fun createSearchUserIntent(): Observable<SearchUserIntent> {
        return Observable.create { emitter ->
            button_search.setOnClickListener {
                val query = input_edit_search.text.toString()
                val page = 1
                val intent = SearchUserIntent.SearchUser(
                    query = query,
                    perPage = SEARCH_USER_DEFAULT_PER_PAGE,
                    page = page
                )

                emitter.onNext(intent)
            }
        }
    }

    private fun createLoadMoreUserIntent(): Observable<SearchUserIntent> {
        return Observable.create { emitter ->
            list_search_result.mOnLoadMoreListener = { page ->
                val intent = SearchUserIntent.LoadMoreUser(
                    query = input_edit_search.text.toString(),
                    perPage = SEARCH_USER_DEFAULT_PER_PAGE,
                    page = page + 1
                )

                emitter.onNext(intent)
            }
        }
    }
}