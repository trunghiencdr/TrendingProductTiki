package com.example.tikitrendingproject.retrofit.repository

import com.example.tikitrendingproject.model.MetaData
import com.example.tikitrendingproject.model.Product
import com.example.tikitrendingproject.retrofit.BaseResponse
import com.example.tikitrendingproject.retrofit.service.TrendingProductService
import kotlinx.coroutines.*

class TrendingProductRepository constructor(private val service: TrendingProductService) {
    fun getTrendingProduct(
        coroutineScope: CoroutineScope,
        cursor: Int,
        limit: Int,
        taskDone: (MetaData?) -> Unit
    ) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val response = service.getTrendingProduct(cursor, limit)
                BaseResponse.process(response) { data ->
                    if (data != null) {
                        taskDone.invoke(data.metaData)
                    } else {
                        taskDone.invoke(null)
                    }
                }
            }
        }
    }

    fun getTrendingProductByCategoryId(
        coroutineScope: CoroutineScope,
        categoryId: Int,
        cursor: Int,
        limit: Int,
        taskDone: (List<Product>?) -> Unit
    ) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val response = service.getProductByCategoryId(
                    categoryId,
                    cursor, limit
                )
                BaseResponse.process(response) { data ->
                    if (data != null) {
                        taskDone.invoke(data.data)
                    } else {
                        taskDone.invoke(null)
                    }
                }
            }
        }
    }

    suspend fun getTrendingProductByCategoryId2(
        coroutineScope: CoroutineScope,
        categoryId: Int,
        cursor: Int,
        limit: Int
    ): List<Product>? {
       var response =  service.getProductByCategoryId(
            categoryId,
            cursor, limit
        )
        if(response.isSuccessful && response.body()?.status==200){
            return response.body()!!.data.data
        }else return listOf()
    }
}