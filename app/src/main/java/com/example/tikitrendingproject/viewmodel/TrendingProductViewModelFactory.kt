package com.example.tikitrendingproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tikitrendingproject.retrofit.repository.TrendingProductRepository

class TrendingProductViewModelFactory constructor(private val trendingProductRepository: TrendingProductRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TrendingProductViewModel::class.java)){
            return TrendingProductViewModel(trendingProductRepository) as T
        }else {
            throw IllegalArgumentException("Unknown View Model class")
        }
    }
}