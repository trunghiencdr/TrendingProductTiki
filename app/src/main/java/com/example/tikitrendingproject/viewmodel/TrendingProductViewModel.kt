package com.example.tikitrendingproject.viewmodel

import android.annotation.SuppressLint
import android.content.res.Resources
import android.media.Image
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.example.tikitrendingproject.R
import com.example.tikitrendingproject.model.Product
import com.example.tikitrendingproject.model.ProductCategory
import com.example.tikitrendingproject.retrofit.RetroInstance
import com.example.tikitrendingproject.retrofit.repository.TrendingProductRepository
import com.example.tikitrendingproject.retrofit.service.TrendingProductService
import com.example.tikitrendingproject.view.Action
import com.example.tikitrendingproject.view.ProductCategoryAdapter
import com.example.tikitrendingproject.view.TrendingProductAdapter
import kotlinx.coroutines.*

class TrendingProductViewModel constructor(
    private val trendingProductRepository: TrendingProductRepository
) :
    ViewModel(), DefaultLifecycleObserver, Action<ProductCategory> {

    companion object {
        val TAG = TrendingProductViewModel::class.java.name
    }

    var _urlBackground= MutableLiveData<String?>()
    var _trendingProduct = MutableLiveData<ArrayList<Product>?>()
    var _trendingProductCategory = MutableLiveData<ArrayList<ProductCategory>?>()
    var errorMessage = MutableLiveData<String>()
    var loading = MutableLiveData<Boolean>()
    var job: Job? = null
    var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(message = "ExceptionHandler: ${throwable.localizedMessage}")
    }

    // set up adapter
    var productCategoryAdapter = ProductCategoryAdapter(this)
    var productAdapter = TrendingProductAdapter()


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
    val urlBackground get() = _urlBackground




    fun callProductCategory(cursor: Int, limit: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = trendingProductRepository.getTrendingProduct(cursor, limit)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _urlBackground.postValue(response.body()?.data?.metaData?.backgroundImage)
                    _trendingProductCategory.postValue(response.body()?.data?.metaData?.items)
                    callTrendingProductByCategoryId(response.body()?.data?.metaData?.items?.get(0)!!.categoryId,0, 20)
//                        loading.value = false
                } else {
                    _trendingProductCategory.postValue(null)
                    _urlBackground.postValue(null)
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
    fun setDataForProduct(it: ArrayList<Product>?) {
        productAdapter.submitList(it)
    }


    fun setImageBackground(imageView: ImageView, it: String?) {
        Glide.with(imageView.context)
            .load(it)
            .into(imageView)

    }

    override fun onClick(t: ProductCategory) {
        callTrendingProductByCategoryId(t.categoryId, 0, 20)
    }

    override fun onCleared() {
        super.onCleared()
    }

    override fun onClickWithBackground(view: View, t: ProductCategory) {
        productCategoryAdapter.oldView?.setBackgroundResource(R.drawable.bg_normal_category)
        view.setBackgroundResource(R.drawable.bg_choosed_category)
        productCategoryAdapter.oldView = view
        callTrendingProductByCategoryId(t.categoryId, 0, 20)
    }

}