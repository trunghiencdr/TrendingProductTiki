package com.example.tikitrendingproject.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.tikitrendingproject.R

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String){
    Glide.with(imageView)
        .load(url)
        .error(R.drawable.error_image)
        .placeholder(R.drawable.error_image)
        .into(imageView)
}