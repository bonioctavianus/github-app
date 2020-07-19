package com.example.github_app.ui.search

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class SearchUserList(context: Context, attributeSet: AttributeSet) :
    RecyclerView(context, attributeSet) {

    var mOnLoadMoreListener: ((page: Int) -> Unit)? = null

    private val mAdapter = SearchUserAdapter()
    private val mLayoutManager = LinearLayoutManager(context)

    private var mTotalItemCount = 0
    private var mCurrentPage = 0
    private var mInProgress = false

    init {
        layoutManager = mLayoutManager
        adapter = mAdapter

        initScrollListener()
    }

    fun submitList(inProgress: Boolean, data: SearchUserData) {
        mTotalItemCount = data.totalItemCount
        mCurrentPage = data.page
        mInProgress = inProgress

        data.cells?.let {
            mAdapter.submitList(it)
        }
    }

    fun getCurrentSearchResultPage(): Int {
        return 1
    }

    private fun initScrollListener() {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) return
                if (mInProgress) return

                val visibleItemCount = mLayoutManager.childCount
                val totalItemCount = mLayoutManager.itemCount
                val pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    mInProgress = true
                    Timber.d("on load more called...")
                    mOnLoadMoreListener?.invoke(mCurrentPage)
                }
            }
        })
    }
}