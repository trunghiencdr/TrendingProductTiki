package com.example.tikitrendingproject.util

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