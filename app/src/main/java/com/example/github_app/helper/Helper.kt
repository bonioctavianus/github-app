package com.example.github_app.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.github_app.R

fun hasNetworkConnectivity(context: Context): Boolean? {
    var isConnected: Boolean? = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected) {
        isConnected = true
    }
    return isConnected
}

fun ImageView.loadImage(url: String?) {
    Glide.with(context)
        .load(url)
        .transform(CenterCrop(), RoundedCorners(24))
        .placeholder(R.drawable.ic_image_black)
        .error(R.drawable.ic_broken_image_black)
        .into(this)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun hideSoftKeyboard(activity: FragmentActivity?) {
    val view = activity?.currentFocus
    view?.let { v ->
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}