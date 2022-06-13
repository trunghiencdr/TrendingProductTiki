package com.example.tikitrendingproject.util

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.tikitrendingproject.R
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.*

//class StringUtil {
//    companion object{
//        fun formatCurrency(number: Int): String? {
////            val locale = Locale("vi", "VN")
////            val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(locale)
////            return currencyFormatter.format(number)
//
//            val format = NumberFormat.getCurrencyInstance()
//            format.maximumFractionDigits = 0
//            format.currency = Currency.getInstance("EUR")
//            return format.format(number)
//        }
//    }
//}

fun formatCurrency(number: Int): String? {
//            val locale = Locale("vi", "VN")
//            val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(locale)
//            return currencyFormatter.format(number)

    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("EUR")
    return format.format(number)
}

fun isNetworkAvailable(context: Context?): Boolean {
    if (context == null) return false
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}

fun onSNACK(view: View){
    //Snackbar(view)
    val snackbar = Snackbar.make(view, "Replace with your own action",
        Snackbar.LENGTH_LONG).setAction("Action", null)
    snackbar.setActionTextColor(Color.BLUE)
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(Color.LTGRAY)
    val textView =
        snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textView.setTextColor(Color.BLUE)
    textView.textSize = 28f
    snackbar.show()
}

fun writeLogDebug(message: String){
    if(Constant.VISIBLE_LOG)
    Log.d(Constant.TAG_DEBUG, message)
}

fun loadBackground(imageView: ImageView, url: String){
    //Picasso.get().load(url).into(view)
    Glide.with(imageView.context)
        .load(url)
        .error(R.drawable.error_image)
        .placeholder(R.drawable.error_image)
        .into(imageView)
}