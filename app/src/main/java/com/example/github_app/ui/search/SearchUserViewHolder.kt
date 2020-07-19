package com.example.github_app.ui.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.github_app.helper.loadImage
import com.example.github_app.model.User
import kotlinx.android.synthetic.main.row_item_user.view.*

class UserViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {

    fun bind(item: User) = with(mView) {
        image_avatar.loadImage(item.avatar)
        text_username.text = item.username
    }
}

class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view)