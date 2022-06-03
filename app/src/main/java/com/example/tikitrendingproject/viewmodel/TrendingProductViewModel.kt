package com.example.tikitrendingproject.viewmodel

import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.tikitrendingproject.model.Product
import com.example.tikitrendingproject.model.ProductCategory
import com.example.tikitrendingproject.retrofit.RetroInstance
import com.example.tikitrendingproject.retrofit.repository.TrendingProductRepository
import com.example.tikitrendingproject.retrofit.service.TrendingProductService
import com.example.tikitrendingproject.view.ProductCategoryAdapter
import kotlinx.coroutines.*

class TrendingProductViewModel constructor(
    private val trendingProductRepository: TrendingProductRepository
) :
    ViewModel(), DefaultLifecycleObserver {

    companion object {
        val TAG = TrendingProductViewModel::class.java.name
    }

    var _trendingProduct = MutableLiveData<ArrayList<Product>?>()
    var _trendingProductCategory = MutableLiveData<ArrayList<ProductCategory>?>()
    var errorMessage = MutableLiveData<String>()
    var loading = MutableLiveData<Boolean>()
    var job: Job? = null
    var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(message = "ExceptionHandler: ${throwable.localizedMessage}")
    }

    // set up adapter
    var productCategoryAdapter = ProductCategoryAdapter()


    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.d(TAG, "call fun onStart")

    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false

    }


    val trendingProduct
        get() = _trendingProduct

    val trendingProductCategory
        get() = _trendingProductCategory




    fun callProductCategory(cursor: Int, limit: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = trendingProductRepository.getTrendingProduct(cursor, limit)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _trendingProductCategory.postValue(response.body()?.data?.metaData?.items)
//                        loading.value = false
                } else {
                    _trendingProductCategory.postValue(null)
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun callTrendingProductByCategoryId(categoryId: Int, cursor: Int, limit: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response =
                trendingProductRepository.getTrendingProductByCategoryId(categoryId, cursor, limit)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _trendingProduct.postValue(response.body()?.data?.data)
                    loading.value = false
                } else {
                    _trendingProduct.postValue(null)
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun setDataForCategory(it: ArrayList<ProductCategory>?) {
        productCategoryAdapter.submitList(it)
    }

}