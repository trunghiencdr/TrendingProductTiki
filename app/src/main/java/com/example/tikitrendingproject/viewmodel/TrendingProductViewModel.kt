package com.example.tikitrendingproject.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
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
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.selects.select
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TrendingProductViewModel constructor(
    private val trendingProductRepository: TrendingProductRepository,
    private val context: Context
) : ViewModel(), DefaultLifecycleObserver, Action<ProductCategory> {


    companion object {
        val TAG = TrendingProductViewModel::class.java.name!!
    }


    private var categorySelected: ProductCategory? = null

    private var queryTextChangeStateFlow = MutableStateFlow("")

    //We will use a ConflatedBroadcastChannel as this will only broadcast
    //the most recent sent element to all the subscribers
    private val topTrendingRepository = TopTrendingRepository(context)
    private val _urlBackground = MutableLiveData<String?>()
    private val _trendingProduct = MutableLiveData<List<Product>?>()
    private val _trendingProductCategory = MutableLiveData<List<ProductCategory>?>()
    private val errorMessage = MutableLiveData<String>()
    private var loading = MutableLiveData<Boolean>()
    private val job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(message = "ExceptionHandler: ${throwable.localizedMessage}")
    }

    // set up adapter
    val productCategoryAdapter = ProductCategoryAdapter(this)
    val productAdapter = TrendingProductAdapter()

    //getter
    val trendingProduct
        get() = _trendingProduct
    val trendingProductCategory
        get() = _trendingProductCategory
    val urlBackground get() = _urlBackground

    fun getLoading() = loading
    fun setLoading(value: Boolean) {
        loading.postValue(value)
    }

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

    fun loadData(cursor: Int, limit: Int) {
        trendingProductRepository.getTrendingProduct(viewModelScope, 0, 20) { metaData ->
            if (metaData != null) {
                _trendingProductCategory.postValue(metaData.items)
//                _urlBackground.postValue(metaData.backgroundImage)
                // save to database local
                saveDataToSql(metaData = metaData)
                metaData.items?.get(0)?.let {
                    callProductsByCategory(it.categoryId) // call list product with first category
                    categorySelected = it
                }

            } else {
                _trendingProductCategory.postValue(null)
//                _urlBackground.postValue(null)
            }
        }
    }


    private fun saveDataToSql(metaData: MetaData) {
        topTrendingRepository.insertMetaDataAndCategory(metaData)
    }

    private fun callTrendingProductByCategoryId(categoryId: Int, cursor: Int, limit: Int) {
        trendingProductRepository.getTrendingProductByCategoryId(
            viewModelScope,
            categoryId,
            cursor,
            limit
        ) { data ->
            if (data != null) {
                _trendingProduct.postValue(data)
                saveProductIntoLocal(categoryId, data)
            } else {
                _trendingProduct.postValue(null)
            }

        }
    }


    private fun saveProductIntoLocal(cateId: Int, products: List<Product>?) {
        GlobalScope.launch {
            val responses = mutableListOf<Deferred<Long>>()
            if (products != null) {
                for (product in products) {
                    async(Dispatchers.IO) {
                        topTrendingRepository.insertProduct(product)
                    }.let { it -> responses.add(it) }
                }
            }

            for (i in responses.indices) {
                select<Unit> {
                    for (response in responses) {
                        response.onAwait { success ->
                            if (success > 0) {
                                products?.get(i)?.let { product ->
                                    product.quantitySold?.productSku = product.productSku
                                    insertQuantitySoldToLocal(product.quantitySold)
                                    insertProductCategoryCrossRef(product.productSku, cateId)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun insertProductCategoryCrossRef(productSku: String, cateId: Int) {
        topTrendingRepository.insertProductCategoryCrossRef(
            ProductCategoryCrossRef(
                productSku,
                cateId
            )
        )

    }

    private fun insertQuantitySoldToLocal(quantitySold: QuantitySold?) {
        topTrendingRepository.insertQuantitySold(quantitySold)
    }

    fun setDataForCategory(it: List<ProductCategory>?) {
        productCategoryAdapter.submitList(it)
    }

    fun setDataForProduct(it: List<Product>?) {
        productAdapter.submitList(it)
    }

    override fun onClick(t: ProductCategory) {
        callTrendingProductByCategoryId(t.categoryId, 0, 20)
    }

    override fun onCleared() {
        super.onCleared()
    }

    override fun onClickWithBackground(view: View, t: ProductCategory) {
        if (view == null) return
        categorySelected = t
        productCategoryAdapter.oldView?.setBackgroundResource(R.drawable.bg_normal_category)
        view.setBackgroundResource(R.drawable.bg_choosed_category)
        productCategoryAdapter.oldView = view

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

    fun setSearchQuery(search: String) {
        // user .offer() to send element to all subscribers
        queryTextChangeStateFlow.value = search
    }

    fun observerSearchView(searchView: SearchView) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                searchView.getQueryTextChangeStateFlow()
                    .debounce(300)
                    .filter { query ->
                        if (query.isEmpty()) {
                            // submit list with all products
                            categorySelected?.categoryId?.let {
                                callTrendingProductByCategoryId(
                                    it,
                                    0,
                                    20
                                )
                            }
                            return@filter false
                        } else {
                            // submit list with products that match query
                            //add loading
                            return@filter true
                        }
                    }
                    .distinctUntilChanged()
                    .flatMapLatest{
                        query->
                        searchProduct(categorySelected?.categoryId!!, query, 0, 20).catch {
                            emitAll(flowOf(emptyList()))
                        }
                    }
                    .flowOn(Dispatchers.IO)
                    .collect {
                        products -> _trendingProduct.postValue(products)
                        loading.postValue(false)
                    }
                        // search for products that match query


            }
        }

    }

    private suspend fun searchProduct(
        categoryId: Int,
        query: String,
        cursor: Int,
        limit: Int
    ): Flow<List<Product>> {
        var listProducts = trendingProductRepository.getTrendingProductByCategoryId2(
            viewModelScope,
            categoryId,
            cursor,
            limit
        )
        listProducts?.let{
            return flowOf(it.filter { it.name.contains(query) or it.price.toString().contains(query) })
        }
        return flowOf(emptyList())
    }

    fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {
        val searchQuery = MutableStateFlow("")
        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    loading.value=true
                    searchQuery.value = newText
                }
                return true
            }
        })
        return searchQuery
    }

}