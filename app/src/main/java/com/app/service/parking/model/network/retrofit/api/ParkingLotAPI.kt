package com.app.service.parking.model.network.retrofit.api

import com.app.service.parking.model.dto.Lot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ParkingLotAPI {

    @GET("/lots/location")
    fun getLotsByLocation(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<ArrayList<Lot>>

    @GET("/lots/address")
    fun getLotsByAddress(
        @Query("addr") query: String? = "",
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<ArrayList<Lot>>
}