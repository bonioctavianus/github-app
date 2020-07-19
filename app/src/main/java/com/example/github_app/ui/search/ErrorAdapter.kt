package com.example.github_app.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.github_app.R
import com.example.github_app.model.Error
import kotlinx.android.synthetic.main.row_item_info.view.*

class ErrorAdapter : RecyclerView.Adapter<ErrorViewHolder>() {

    private var mError: Error? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ErrorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_info, parent, false)

        return ErrorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (mError != null) 1 else 0
    }

    override fun onBindViewHolder(holder: ErrorViewHolder, position: Int) {
        mError?.let { holder.bind(it) }
    }

    fun submitErrorState(error: Error?) {
        mError = error
        notifyDataSetChanged()
    }
}

class ErrorViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Error) = with(itemView) {
        image_info.setImageResource(item.image)
        text_info.text = item.message
    }
}