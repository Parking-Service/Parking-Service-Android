package com.app.buna.foodplace.model.network.retrofit.api

import com.app.buna.foodplace.model.dto.ParkingLot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ParkingLotAPI {

    @GET("/openapi/tn_pubr_prkplce_info_api")
    fun getParkingLot(
        @Query("serviceKey") apiKey: String,
        @Query("pageNo") pageNo: Int = 0,
        @Query("numOfRows") numOfRows: Int = 100,
        @Query("type") type: String = "json"
    ): Call<ParkingLot>
}