package com.example.tikitrendingproject.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tikitrendingproject.model.Data
import com.example.tikitrendingproject.model.Product
import com.example.tikitrendingproject.model.ResponseObject
import com.example.tikitrendingproject.retrofit.RetroInstance
import com.example.tikitrendingproject.retrofit.repository.TrendingProductRepository
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import retrofit2.Retrofit

class TrendingProductViewModel: ViewModel() {

    private lateinit var _trendingProduct: MutableLiveData<List<Product>>
    private lateinit var trendingProductRepository: TrendingProductRepository
    init {
        trendingProductRepository = RetroInstance.getInstance().create(TrendingProductRepository::class.java)
        _trendingProduct = MutableLiveData()
    }

    val trendingProduct
        get()=_trendingProduct

//    @SuppressLint("CheckResult")
//    fun callTrendingProduct(){
//        trendingProductRepository.getTrendingProduct(0, 20)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : SingleObserver<Response<ResponseObject<Data>>>){
//
//            }
//
//    }
}