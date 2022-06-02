package com.example.tikitrendingproject.retrofit

import androidx.annotation.Nullable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetroInstance {

    companion object{

        val BASE_URL = "https://api.tiki.vn/"
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        var INSTANCE: Retrofit?=null

        fun getInstance(): Retrofit{
            if(INSTANCE==null){
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }

            return INSTANCE as Retrofit
        }
    }
}