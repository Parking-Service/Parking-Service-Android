package com.app.service.parking.model.network.retrofit.api

import com.app.service.parking.model.dto.Lot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ParkingLotAPI {

    @GET("/lots/location")
    fun getLots(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<List<Lot>>

}