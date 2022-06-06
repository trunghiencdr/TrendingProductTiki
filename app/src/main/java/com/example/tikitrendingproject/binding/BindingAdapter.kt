package com.example.tikitrendingproject.binding

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableResource
import com.example.tikitrendingproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.*

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String){
    Glide.with(imageView.context)
        .load(url)
        .error(R.drawable.error_image)
        .placeholder(R.drawable.error_image)
        .into(imageView)
}

@BindingAdapter("formatCurrency")
fun formatCurrency(textView: TextView, value: Int){
    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("VND")
    textView.text = format.format(value)
}

//@BindingAdapter("loadBackground")
//fun loadBackground(layout: ConstraintLayout, url: String){
//    var job = CoroutineScope(Dispatchers.IO).launch {
//        val theBitmap: Bitmap =
//            Glide.with(layout.context).
//            asBitmap().
//            load(url).
//            submit().
//            get() // Width and height
//        withContext(Dispatchers.Main){
//            layout.setBackgroundResource(BitmapDrawable(theBitmap))
//        }
//    }
//}