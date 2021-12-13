package com.app.service.parking.model.repository.remote

import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.network.retrofit.builder.RetrofitParkingAPIBuilder
import kotlinx.coroutines.flow.Flow

class ParkingLotRepository {

    // 검색 값(query)을 바탕으로 스프링부트 서버에 주차장 데이터 요청
    suspend fun getLotsFlow(query: String?, latitude: Double, longitude: Double): Flow<ArrayList<Lot>> {
        return RetrofitParkingAPIBuilder.getParkingLotsByQuery(query, latitude, longitude)
    }

    // 전화번호(number)를 바탕으로 스프링부트 서버에 주차장 데이터 요청
    suspend fun getLotsFlow(number: String): Flow<ArrayList<Lot>> {
        return RetrofitParkingAPIBuilder.getParkingLotsByNumber(number)
    }
}