package com.example.tikitrendingproject.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tikitrendingproject.retrofit.repository.TrendingProductRepository
import com.example.tikitrendingproject.room.repository.MetaDataRepository

class MetaDataViewModelFactory constructor(
    private val metaDataRepository: MetaDataRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MetaDataViewModel::class.java)) {
            return MetaDataViewModel(metaDataRepository) as T
        } else {
            throw IllegalArgumentException("Unknown View Model class")
        }
    }
}