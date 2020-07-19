package com.example.github_app.ui.search

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.github_app.R
import com.example.github_app.model.Cell
import com.example.github_app.model.Progress
import com.example.github_app.model.User
import java.util.*

enum class UserAdapterViewType {
    VIEW_TYPE_ITEM,
    VIEW_TYPE_PROGRESS
}

class SearchUserAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mItems: MutableList<Cell> = mutableListOf()
    private val pendingUpdates: Deque<List<Cell>> = ArrayDeque()

    override fun getItemViewType(position: Int): Int {
        return when (mItems[position]) {
            is User -> UserAdapterViewType.VIEW_TYPE_ITEM.ordinal
            is Progress -> UserAdapterViewType.VIEW_TYPE_PROGRESS.ordinal
            else -> throw IllegalStateException("Unknown view type for: ${mItems[position]}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            UserAdapterViewType.VIEW_TYPE_ITEM.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_item_user, parent, false)

                UserViewHolder(view)
            }

            UserAdapterViewType.VIEW_TYPE_PROGRESS.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_item_progress_bar, parent, false)

                ProgressViewHolder(view)
            }

            else -> throw IllegalStateException("Unknown view type: $viewType")
        }
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserViewHolder -> holder.bind(mItems[position] as User)
            is ProgressViewHolder -> Unit
            else -> throw IllegalStateException("Unknown view holder: $holder")
        }
    }

    fun submitList(items: List<Cell>) {
        pendingUpdates.push(items)
        if (pendingUpdates.size > 1) return
        updateItemsInternal(items)
    }

    private fun updateItemsInternal(newItems: List<Cell>) {
        val oldItems = mItems.toList()
        val handler = Handler()

        Thread(
            Runnable {
                val diffCallback = UserDiffCallback(oldItems, newItems)
                val diffResult = DiffUtil.calculateDiff(diffCallback)
                handler.post { applyDiffResult(newItems, diffResult) }
            }).start()
    }

    private fun applyDiffResult(newItems: List<Cell>, diffResult: DiffUtil.DiffResult) {
        pendingUpdates.removeLast()
        dispatchUpdates(newItems, diffResult)

        if (pendingUpdates.isNotEmpty()) {
            updateItemsInternal(pendingUpdates.peek())
        }
    }

    private fun dispatchUpdates(newItems: List<Cell>, diffResult: DiffUtil.DiffResult) {
        diffResult.dispatchUpdatesTo(this)
        mItems.clear()
        mItems.addAll(newItems)
    }
}