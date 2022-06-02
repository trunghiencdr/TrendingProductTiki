package com.example.tikitrendingproject.retrofit.repository

import com.example.tikitrendingproject.model.Data
import com.example.tikitrendingproject.model.ResponseObject
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendingProductRepository {

    @GET("shopping-trend/api/trendings/hub?cursor=0&limit=20")
    fun getTrendingProduct(
                            @Query("cursor") cursor:Int,
                            @Query("limit") limit:Int): ResponseObject<Data>
}