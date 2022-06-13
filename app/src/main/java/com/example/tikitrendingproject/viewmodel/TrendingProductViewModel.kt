package com.example.tikitrendingproject.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.example.tikitrendingproject.R
import com.example.tikitrendingproject.model.*
import com.example.tikitrendingproject.retrofit.BaseResponse
import com.example.tikitrendingproject.retrofit.repository.TrendingProductRepository
import com.example.tikitrendingproject.room.repository.TopTrendingRepository
import com.example.tikitrendingproject.util.isNetworkAvailable
import com.example.tikitrendingproject.view.Action
import com.example.tikitrendingproject.view.ProductCategoryAdapter
import com.example.tikitrendingproject.view.TrendingProductAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TrendingProductViewModel constructor(
    private val trendingProductRepository: TrendingProductRepository,
    private val context: Context
) : ViewModel(), DefaultLifecycleObserver, Action<ProductCategory> {

    companion object {
        val TAG = TrendingProductViewModel::class.java.name
    }

    private val topTrendingRepository = TopTrendingRepository(context)


    private val _urlBackground = MutableLiveData<String?>()
    private val _trendingProduct = MutableLiveData<List<Product>?>()
    private val _trendingProductCategory = MutableLiveData<List<ProductCategory>?>()
    private val errorMessage = MutableLiveData<String>()
    private val loading = MutableLiveData<Boolean>()
    private val job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(message = "ExceptionHandler: ${throwable.localizedMessage}")
    }

    // set up adapter
    val productCategoryAdapter = ProductCategoryAdapter(this)
    val productAdapter = TrendingProductAdapter()


    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.d(TAG, "call fun onStart")
        if (isNetworkAvailable(context)) {
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        Log.d("HIEN", message)
        loading.postValue(false)

    }


    val trendingProduct
        get() = _trendingProduct
    val trendingProductCategory
        get() = _trendingProductCategory
    val urlBackground get() = _urlBackground


    fun loadData(cursor: Int, limit: Int) {
        viewModelScope.launch {
            loading.value = true
            withContext(Dispatchers.IO + exceptionHandler) {
                val result = getTrendingProductCategory(cursor, limit)
                BaseResponse.process(result) { data ->
                    data?.let {
                        with(_trendingProductCategory) { postValue(data.metaData.items) }
                        _urlBackground.postValue(data.metaData.backgroundImage)

                        // save to database local
                        saveDataToSql(metaData = data.metaData)
                        callProductsByCategory(data.metaData.items?.get(0)!!.categoryId) // call list product with first category
                    }
                }
            }
        }
    }

    private suspend fun getTrendingProductCategory(
        cursor: Int,
        limit: Int
    ): Response<ResponseObject<Data>> {
        return trendingProductRepository.getTrendingProduct(cursor, limit)
    }

    private fun saveDataToSql(metaData: MetaData) {
        GlobalScope.launch {
            withContext(Dispatchers.IO + exceptionHandler) {
                try {
                    topTrendingRepository.insertMetaData(metaData)
                } catch (e: Exception) {
                    Log.d("HIEN", e.localizedMessage)
                }
                // can optimize with flow?
                // Does it run linear or parallel ?
                metaData.items?.forEach { it ->
                    topTrendingRepository.insertProductCategory(it)
                }
            }
        }

    }


    private fun callTrendingProductByCategoryId(categoryId: Int, cursor: Int, limit: Int) {
        loading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO + exceptionHandler) {
                val result = getProductsByCategory(categoryId, cursor, limit)
                BaseResponse.process(result) { data ->
                    data?.let {
                        _trendingProduct.postValue(data.data)
                        saveProductIntoLocal(categoryId, data.data)
                    }
                }
            }
        }
    }

    private suspend fun getProductsByCategory(categoryId: Int, cursor: Int, limit: Int): Response<ResponseObject<Data>> {
        return trendingProductRepository.getTrendingProductByCategoryId(categoryId, cursor, limit)
    }

    private fun saveProductIntoLocal(cateId: Int, products: List<Product>?) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                products?.forEach { product ->
                    // insert product into room
                    var kq = topTrendingRepository.insertProduct(product)
                    // if insert product success then insert quantity sold and product with category
                    // insert quantitySold of product
                    if (kq > 0) {
                        product.apply {
                            quantitySold?.productSku = productSku
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            topTrendingRepository.insertQuantitySold(product.quantitySold)
                            // insert product with category
                            launch {
                                topTrendingRepository.insertProductCategoryCrossRef(
                                    ProductCategoryCrossRef(
                                        product.productSku,
                                        cateId
                                    )
                                )
                            }
                        }
                    }
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
        if (isNetworkAvailable(context))
            callTrendingProductByCategoryId(t.categoryId, 0, 20)
        else {
            callProductsByCategory(t.categoryId)
        }
    }

    fun callDataFromLocal(view: View) {
        Snackbar.make(view, "Your are in offline", Snackbar.LENGTH_LONG).show()
        callBackground()
        callListCategory()
    }

    fun callBackground() {
        CoroutineScope(Dispatchers.IO).launch {
            var listMeta = topTrendingRepository.findMetaDataByType("top_trending")
            withContext(Dispatchers.Main) {
                if (listMeta.size > 0) {
                    _urlBackground.postValue(listMeta[0].backgroundImage)
                } else {
                    _urlBackground.postValue("")
                }
            }
        }
    }

    fun callListCategory() {
        runBlocking {
            val job = launch {
                var listCategory = topTrendingRepository.getAllProductCategory()
                if (listCategory.size > 0) {
                    callProductsByCategory(listCategory.get(0).category.categoryId)
                    _trendingProductCategory.postValue(
                        listCategory.map {
                            ProductCategory(it.category.title, it.category.categoryId,
                                it.image.map {
                                    it.url
                                })
                        }
                    )
                } else {
                    _trendingProductCategory.postValue(null)
                }
            }
            job.cancelAndJoin()
        }
    }


    fun callProductsByCategory(id: Int) {
        var listProducts: MutableList<Product> = ArrayList()
        CoroutineScope(Dispatchers.IO).launch {
            var listCategoryWithProducts = topTrendingRepository.findByCategoryId(id)
            withContext(Dispatchers.Main) {
                if (listCategoryWithProducts.size > 0) {
                    listCategoryWithProducts.forEach {
                        var product = topTrendingRepository.findProductsBySku(it.productSku)
                        var quantitySold =
                            topTrendingRepository.findQuantitySoldByProductSku(it.productSku)
                        product.quantitySold = quantitySold
                        listProducts.add(product)
                    }
                }

                if (listProducts.size > 0) {
                    _trendingProduct.postValue(listProducts)
                } else {
                    _trendingProduct.postValue(null)
                }
            }
        }
    }

}