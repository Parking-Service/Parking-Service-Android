package com.app.service.parking.model.network.retrofit.builder

import com.app.service.parking.model.preference.ParkingPreference
import com.app.service.parking.model.preference.PreferenceConst
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class BaseRetrofitBuilder {
    open val baseUrl = "http://49.165.181.24:8080"
    open val TIMEOUT_LIMIT = 5L
    private val gson = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .setLenient()
        .create()

    private val clientBuilder =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_LIMIT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_LIMIT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_LIMIT, TimeUnit.SECONDS)
            //.authenticator(TokenAuthenticator(App.context!!, ParkingPreference.getString(PreferenceConst.TOKEN.name, "")!!))
            .addInterceptor(AuthInterceptor())
            .addInterceptor(
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

    class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = ParkingPreference.getString(PreferenceConst.TOKEN.name, "")
            var req = chain.request().newBuilder().addHeader("Authorization", token)
                .build()
            return chain.proceed(req)
        }
    }

}