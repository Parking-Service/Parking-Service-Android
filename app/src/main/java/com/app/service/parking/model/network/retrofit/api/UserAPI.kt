package com.app.service.parking.model.network.retrofit.api

import com.app.service.parking.model.dto.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserAPI {
    // 회원가입
    @POST("/user/register")
    fun getRegisterResponse(@Body user: User): Call<Void>

    // 유저 데이터 가져오기
    @GET("/user/logindata")
    fun getUserLoginData(): Call<Void>
}