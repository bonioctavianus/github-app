package com.example.github_app.ui.search

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.github_app.model.Error
import com.example.github_app.model.Info
import com.example.github_app.model.User
import timber.log.Timber

class SearchUserList(context: Context, attributeSet: AttributeSet) :
    RecyclerView(context, attributeSet) {

    var mOnLoadMoreListener: ((page: Int) -> Unit)? = null

    private val mUserAdapter = UserAdapter()
    private val mProgressAdapter = ProgressAdapter()
    private val mEmptyAdapter = EmptyAdapter()
    private val mErrorAdapter = ErrorAdapter()

    private val mAdapter = ConcatAdapter(
        mUserAdapter, mProgressAdapter, mEmptyAdapter, mErrorAdapter
    )

    private val mLayoutManager = LinearLayoutManager(context)

    private var mTotalItemCount = 0
    private var mCurrentPage = 0
    private var mIsLoading = false

    init {
        layoutManager = mLayoutManager
        adapter = mAdapter

        initScrollListener()
    }

    fun submitLoadingState(isLoading: Boolean) {
        mIsLoading = isLoading
        mProgressAdapter.submitLoadingState(isLoading)
    }

    fun submitEmptyState(info: Info?) {
        mEmptyAdapter.submitEmptyState(info)
    }

    fun submitErrorState(error: Error?) {
        mErrorAdapter.submitErrorState(error)
    }

    fun submitUsers(totalItemCount: Int, page: Int, users: List<User>) {
        mTotalItemCount = totalItemCount
        mCurrentPage = page
        mUserAdapter.submitList(users)
    }

    private fun initScrollListener() {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) return
                if (mIsLoading) return

                val visibleItemCount = mLayoutManager.childCount
                val totalItemCount = mLayoutManager.itemCount
                val pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    mIsLoading = true
                    Timber.d("on load more called...")
                    mOnLoadMoreListener?.invoke(mCurrentPage)
                }
            }
        })
    }
}