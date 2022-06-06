package com.example.tikitrendingproject.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tikitrendingproject.retrofit.repository.TrendingProductRepository

class TrendingProductViewModelFactory constructor(
    private val trendingProductRepository: TrendingProductRepository,
    private val context: Context
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrendingProductViewModel::class.java)) {
            return TrendingProductViewModel(trendingProductRepository, context) as T
        } else {
            throw IllegalArgumentException("Unknown View Model class")
        }
    }
}