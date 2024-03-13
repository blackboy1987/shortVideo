package com.bootx.util

import com.bootx.config.Config
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object HiRetrofit {

    private val mClient: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(100, TimeUnit.SECONDS)
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .writeTimeout(100, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(MyInterceptor())
        .followRedirects(false)
        .build()

    private var retrofit: Retrofit = Retrofit.Builder()
        .client(mClient)
        .baseUrl(Config.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(clazz: Class<T>):T {
        return retrofit.create(clazz)
    }
}