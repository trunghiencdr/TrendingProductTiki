package com.example.tikitrendingproject.retrofit

import com.example.tikitrendingproject.retrofit.service.TrendingProductService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetroInstance {
        private const val BASE_URL = "https://api.tiki.vn/"
        private val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


        fun retrofit(): Retrofit{
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }



        val trendingProductService: TrendingProductService by lazy {
            retrofit().create(TrendingProductService::class.java)
        }

}