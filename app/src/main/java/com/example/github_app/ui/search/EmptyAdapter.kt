package com.example.github_app.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.github_app.R
import com.example.github_app.model.Info
import kotlinx.android.synthetic.main.row_item_info.view.*

class EmptyAdapter : RecyclerView.Adapter<EmptyViewHolder>() {

    private var mInfo: Info? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_info, parent, false)

        return EmptyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (mInfo != null) 1 else 0
    }

    override fun onBindViewHolder(holder: EmptyViewHolder, position: Int) {
        mInfo?.let { holder.bind(it) }
    }

    fun submitEmptyState(info: Info?) {
        mInfo = info
        notifyDataSetChanged()
    }
}

class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Info) = with(itemView) {
        image_info.setImageResource(item.image)
        text_info.setText(item.message)
    }
}