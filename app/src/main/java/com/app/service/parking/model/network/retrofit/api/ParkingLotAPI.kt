package com.app.service.parking.model.network.retrofit.api

import com.app.service.parking.model.dto.Lot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ParkingLotAPI {

    // 좌표로 검색
    @GET("/lots/location")
    fun getLotsByLocation(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<ArrayList<Lot>>

    // 주소로 검색
    @GET("/lots/address")
    fun getLotsByAddress(
        @Query("addr") query: String? = "",
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<ArrayList<Lot>>

    // 전화번호로 검색
    @GET("/lots/tel")
    fun getLotsByNumber(
        @Query("number") number: String
    ): Call<ArrayList<Lot>>
}