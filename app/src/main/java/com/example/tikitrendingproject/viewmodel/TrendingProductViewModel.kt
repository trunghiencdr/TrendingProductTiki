package com.example.tikitrendingproject.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.media.Image
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.*
import androidx.room.Database
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.example.tikitrendingproject.R
import com.example.tikitrendingproject.model.*
import com.example.tikitrendingproject.retrofit.RetroInstance
import com.example.tikitrendingproject.retrofit.repository.TrendingProductRepository
import com.example.tikitrendingproject.retrofit.service.TrendingProductService
import com.example.tikitrendingproject.room.DatabaseBuilder
import com.example.tikitrendingproject.room.MyRoomDatabase
import com.example.tikitrendingproject.room.dao.ImageDao
import com.example.tikitrendingproject.room.repository.MetaDataRepository
import com.example.tikitrendingproject.room.repository.TopTrendingRepository
import com.example.tikitrendingproject.util.isNetworkAvailable
import com.example.tikitrendingproject.util.onSNACK
import com.example.tikitrendingproject.view.Action
import com.example.tikitrendingproject.view.ProductCategoryAdapter
import com.example.tikitrendingproject.view.TrendingProductAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.util.concurrent.CountDownLatch
import java.util.stream.Collectors

class TrendingProductViewModel constructor(
    private val trendingProductRepository: TrendingProductRepository,
    private val context: Context
) :
    ViewModel(), DefaultLifecycleObserver, Action<ProductCategory> {

    companion object {
        val TAG = TrendingProductViewModel::class.java.name
    }

    var topTrendingRepository = TopTrendingRepository(context)


    var _urlBackground= MutableLiveData<String?>()
    var _trendingProduct = MutableLiveData<List<Product>?>()
    var _trendingProductCategory = MutableLiveData<List<ProductCategory>?>()
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
        if(isNetworkAvailable(context)){

        }

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
                   response.body()?.data?.metaData?.also {
                        _urlBackground.postValue(it.backgroundImage)
                        _trendingProductCategory.postValue(it.items)
                        callTrendingProductByCategoryId(it.items?.get(0)!!.categoryId,0, 20)
                        saveDataToSql(it)
                    }
//                        loading.value = false
                } else {
                    _trendingProductCategory.postValue(null)
                    _urlBackground.postValue(null)
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    private suspend fun saveDataToSql(metaData: MetaData) {
        topTrendingRepository.insertMetaData(metaData)
        metaData.items?.forEach{
            it -> topTrendingRepository.insertProductCategory(it)
        }
    }


    fun callTrendingProductByCategoryId(categoryId: Int, cursor: Int, limit: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response =
                trendingProductRepository.getTrendingProductByCategoryId(categoryId, cursor, limit)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.data?.data.also {
                        _trendingProduct.postValue(it)
                        saveProductIntoLocal(categoryId, it)
                    }
                    loading.value = false
                } else {
                    _trendingProduct.postValue(null)
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    private suspend fun saveProductIntoLocal(cateId: Int, products: List<Product>?) {
        products?.forEach {
            it.apply {
                quantitySold?.productSku = productSku
            }
            CoroutineScope(Dispatchers.IO).launch {
                var kq = topTrendingRepository.insertProduct(it)
                withContext(Dispatchers.IO){
                    Log.d("insert product", "$kq ")
                    topTrendingRepository.insertQuantitySold(it.quantitySold)
                    topTrendingRepository.insertProductCategoryCrossRef(ProductCategoryCrossRef(it.productSku, cateId))
                }
            }

        }


    }

    fun setDataForCategory(it: List<ProductCategory>?) {
        productCategoryAdapter.submitList(it)
    }
    fun setDataForProduct(it: List<Product>?) {
        productAdapter.submitList(it)
    }


    fun setImageBackground(imageView: ImageView, it: String?) {
        Glide.with(imageView.context)
            .load(it)
            .error(R.drawable.error_image)
            .placeholder(R.drawable.error_image)
            .into(imageView)

    }

    override fun onClick(t: ProductCategory) {
        callTrendingProductByCategoryId(t.categoryId, 0, 20)
    }

    override fun onCleared() {
        super.onCleared()
    }

    override fun onClickWithBackground(view: View, t: ProductCategory) {
        productCategoryAdapter.oldView.setBackgroundResource(R.drawable.bg_normal_category)
        productCategoryAdapter.oldView = view
        view.setBackgroundResource(R.drawable.bg_choosed_category)
        if(isNetworkAvailable(context))
            callTrendingProductByCategoryId(t.categoryId, 0, 20)
        else{
            callProductsByCategory(t.categoryId)
        }
    }

    fun callDataFromLocal(view: View) {
        Snackbar.make(view, "Your are in offline", Snackbar.LENGTH_LONG).show()
        callBackground()
        callListCategory()


    }

    fun callBackground(){
        CoroutineScope(Dispatchers.IO).launch {
            var listMeta = topTrendingRepository.findMetaDataByType("top_trending")
            withContext(Dispatchers.Main){
                if(listMeta.size>0){
                    _urlBackground.postValue(listMeta[0].backgroundImage)
                }else{
                    _urlBackground.postValue("")
                }
            }
        }
    }

    fun callListCategory(){
        CoroutineScope(Dispatchers.IO).launch {
            var listCategory = topTrendingRepository.getAllProductCategory()
            withContext(Dispatchers.Main){
                if(listCategory.size>0){
                    callProductsByCategory(listCategory.get(0).category.categoryId)
                    _trendingProductCategory.postValue(
                        listCategory.map {
                            ProductCategory(it.category.title, it.category.categoryId,
                            it.image.map {
                                it.url
                            })
                        }
                    )
                }else{

                    _trendingProductCategory.postValue(null)
                }
            }
        }
    }

     fun callProductsByCategory(id: Int){
        var listProducts:MutableList<Product> = ArrayList()
        CoroutineScope(Dispatchers.IO).launch {
            var listCategoryWithProducts = topTrendingRepository.findByCategoryId(id)
            withContext(Dispatchers.Main){
                if(listCategoryWithProducts.size>0){
                    listCategoryWithProducts.forEach {
                        var product = topTrendingRepository.findProductsBySku(it.productSku)
                        var quantitySold = topTrendingRepository.findQuantitySoldByProductSku(it.productSku)
                        product.quantitySold = quantitySold
                        listProducts.add(product)
                    }
                }

                if(listProducts.size>0){
                    _trendingProduct.postValue(listProducts)
                }else{
                    _trendingProduct.postValue(null)
                }
            }
        }
    }

}