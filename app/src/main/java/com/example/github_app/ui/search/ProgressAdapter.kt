package com.example.github_app.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.github_app.R

class ProgressAdapter : RecyclerView.Adapter<ProgressViewHolder>() {

    private var mIsLoading = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_progress_bar, parent, false)

        return ProgressViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (mIsLoading) 1 else 0
    }

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) = Unit

    fun submitLoadingState(isLoading: Boolean) {
        mIsLoading = isLoading
        notifyDataSetChanged()
    }
}

class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view)