package com.example.tikitrendingproject.retrofit.service

import com.example.tikitrendingproject.model.Data
import com.example.tikitrendingproject.model.ProductCategory
import com.example.tikitrendingproject.model.ResponseObject
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrendingProductService {

    @GET("shopping-trend/api/trendings/hub?")
    suspend fun getTrendingProduct(
                            @Query("cursor") cursor:Int,
                            @Query("limit") limit:Int): Response<ResponseObject<Data>>

    @GET("shopping-trend/api/trendings/hub/category_id/{id}?")
    suspend fun getProductByCategoryId(
        @Path("id") categoryId: Int,
        @Query("cursor") cursor:Int,
        @Query("limit") limit:Int
    ): Response<ResponseObject<Data>>

    @GET("shopping-trend/api/trendings/hub/category_id/{id}?")
     fun getProductByCategoryIdWithRx(
        @Path("id") categoryId: Int,
        @Query("cursor") cursor:Int,
        @Query("limit") limit:Int
    ): Observable<Response<ResponseObject<Data>>>
}