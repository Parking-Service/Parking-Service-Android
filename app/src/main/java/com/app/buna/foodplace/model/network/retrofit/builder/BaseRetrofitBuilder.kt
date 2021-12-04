package com.app.buna.foodplace.model.network.retrofit.builder

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseRetrofitBuilder {
    open val baseUrl = "http://49.165.181.24:8080"
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val clientBuilder = OkHttpClient.Builder().addInterceptor(
        HttpLoggingInterceptor().apply {
            // body 로그를 출력하기 위한 interceptor
            level = HttpLoggingInterceptor.Level.BODY
        }
    )

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(clientBuilder.build())
        .build()

    protected fun getRetrofit(): Retrofit {
        return retrofit
    }

}