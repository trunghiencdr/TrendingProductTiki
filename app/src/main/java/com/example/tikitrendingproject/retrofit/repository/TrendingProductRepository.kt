package com.example.tikitrendingproject.retrofit.repository

import com.example.tikitrendingproject.retrofit.service.TrendingProductService

class TrendingProductRepository constructor(private val service: TrendingProductService) {
    suspend fun getTrendingProduct(cursor: Int, limit: Int) = service.getTrendingProduct(cursor,limit)
    suspend fun getTrendingProductByCategoryId(categoryId: Int, cursor: Int, limit: Int) =
        service.getTrendingProductByCategoryId(categoryId, cursor,limit)

}