package com.example.github_app.ui.search

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.github_app.R
import com.example.github_app.helper.gone
import com.example.github_app.helper.hideSoftKeyboard
import com.example.github_app.helper.visible
import com.example.github_app.model.Progress
import io.reactivex.Observable
import kotlinx.android.synthetic.main.component_search_user.view.*
import kotlinx.android.synthetic.main.layout_info.view.*
import timber.log.Timber

class SearchUserComponent(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {

    companion object {
        private const val SEARCH_USER_DEFAULT_PER_PAGE = 20
    }

    var mActivity: FragmentActivity? = null

    init {
        View.inflate(context, R.layout.component_search_user, this)
    }

    fun renderViewState(state: SearchUserViewState) = with(state) {
        when {
            initialError != null -> renderInitialError(errorImage, initialError)
            loadMoreError != null -> renderLoadMoreError(loadMoreError, data)
            else -> renderSearchResult(state)
        }
    }

    private fun renderInitialError(image: Int?, message: String?) {
        Timber.e("render initial error: $message")

        image?.let { image_info.setImageResource(it) }
        message?.let { text_info.text = it }

        container_info.visible()
        list_search_result.gone()
    }

    private fun renderLoadMoreError(error: String?, data: SearchUserData?) {
        Timber.e("render load more error: $error")

        data?.let {
            list_search_result.submitList(false, it)
        }

        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    private fun renderSearchResult(state: SearchUserViewState) = with(state) {
        data?.cells ?: return

        val inProgress = data.cells.contains(Progress)
        val totalItemCount = data.totalItemCount
        val cells = data.cells

        renderTotalItemCount(isTotalItemLabelVisible, totalItemCount)

        if (cells.isNotEmpty()) {
            renderLoadedResult(inProgress, data)

        } else {
            val image = data.emptyImage
            val message = data.emptyMessage

            renderEmptyResult(image, message)
        }
    }

    private fun renderTotalItemCount(isVisible: Boolean, totalItemCount: Int) {
        if (isVisible) {
            val totalItemText = context.getString(
                R.string.search_user_result_total_item_count,
                totalItemCount
            )

            text_total_item_count.text = totalItemText
            text_total_item_count.visible()

        } else {
            text_total_item_count.gone()
        }
    }

    private fun renderEmptyResult(image: Int, message: Int) {
        image_info.setImageResource(image)
        text_info.setText(message)
        container_info.visible()
        list_search_result.gone()
    }

    private fun renderLoadedResult(inProgress: Boolean, data: SearchUserData) {
        list_search_result.submitList(inProgress, data)
        list_search_result.visible()
        container_info.gone()
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
                val page = list_search_result.getCurrentSearchResultPage()
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