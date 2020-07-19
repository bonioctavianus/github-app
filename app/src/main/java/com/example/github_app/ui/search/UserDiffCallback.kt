package com.example.github_app.ui.search

import androidx.recyclerview.widget.DiffUtil
import com.example.github_app.model.Cell
import com.example.github_app.model.User

class UserDiffCallback(
    private val oldList: List<Cell>,
    private val newList: List<Cell>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return when {
            oldItem is User && newItem is User -> oldItem.id == newItem.id
            else -> true
        }
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return when {
            oldItem is User && newItem is User -> oldItem == newItem
            else -> true
        }
    }
}