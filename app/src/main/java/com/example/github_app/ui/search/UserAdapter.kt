package com.example.github_app.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.github_app.R
import com.example.github_app.helper.loadImage
import com.example.github_app.model.User
import kotlinx.android.synthetic.main.row_item_user.view.*

class UserAdapter : RecyclerView.Adapter<UserViewHolder>() {

    private var mItems: MutableList<User> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_user, parent, false)

        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mItems[position])
    }

    fun submitList(items: List<User>) {
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }
}

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: User) = with(itemView) {
        image_avatar.loadImage(item.avatar)
        text_username.text = item.username
    }
}