package com.app.buna.foodplace.model.network.retrofit.api

import com.app.buna.foodplace.model.dto.ParkingLot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ParkingLotAPI {

    @GET("/lots")
    fun getLots(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<ParkingLot>

}