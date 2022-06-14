package com.example.tikitrendingproject.retrofit

import android.content.Context
import android.util.Log
import com.example.tikitrendingproject.util.isNetworkAvailable
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetroInstance {
    private const val BASE_URL = "https://api.tiki.vn/"
    private const val CACHE_SIZE = (10 * 1024 * 1024).toLong()
    private const val CACHE_CONTROL = "Cache-Control"
    private const val TIME_CACHE_ONLINE = "public, max-age = 60" // 1 minute
    private const val READ_TIMEOUT = 5000L
    private const val WRITE_TIMEOUT = 5000L
    private const val CONNECT_TIMEOUT = 5000L

    private const val TIME_CACHE_OFFLINE = "public, only-if-cached, max-stale = 86400" //1 day

    private fun initClient(context: Context): OkHttpClient? {
        val builder = OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .cache(Cache(context.cacheDir, CACHE_SIZE))
            .addInterceptor { chain: Interceptor.Chain ->
                var request: Request = chain.request()
                request = if (isNetworkAvailable(context)) {
                    request
                        .newBuilder()
                        .header(CACHE_CONTROL, TIME_CACHE_ONLINE)
                        .build()
                } else {
                    request
                        .newBuilder()
                        .header(CACHE_CONTROL, TIME_CACHE_OFFLINE)
                        .build()
                }
                val httpUrl: HttpUrl = request.url()
                    .newBuilder() //                            .addQueryParameter(QUERRY_PARAMETER_API_KEY, API_KEY)
                    .build()

                val requestBuilder: Request.Builder = request
                    .newBuilder()
                    .url(httpUrl)
                chain.proceed(requestBuilder.build())
            }
        return builder.build()
    }
    fun getClient(context: Context) = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)

        .build()


    private var INSTANCE: Retrofit? = null

    fun getInstance(context: Context): Retrofit {
        if (INSTANCE == null) {
            synchronized(Retrofit::class) {
                INSTANCE = retrofit(context)
            }
        }

        return INSTANCE!!

    }

    fun retrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(initClient(context))
            .build()
    }


    inline fun <reified T> getService(context: Context): T {
        return getInstance(context).create(T::class.java)
    }

}