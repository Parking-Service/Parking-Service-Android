package com.app.service.parking.model.network.retrofit.builder

import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.network.retrofit.api.ParkingLotAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import kotlin.coroutines.resumeWithException

object RetrofitParkingAPIBuilder : BaseRetrofitBuilder() {

    // 위도, 경도를 바탕으로 주차장 리스트를 가져옴
    suspend fun getParkingLots(latitude: Double, longitude: Double) =

        // 데이터를 가져올 동안, 코루틴 잠시 일시 중지
        suspendCancellableCoroutine<ArrayList<Lot>> { continuation ->
            val api = getRetrofit().create(ParkingLotAPI::class.java)
            api.getLotsByLocation(latitude, longitude).enqueue(object : Callback<ArrayList<Lot>> {
                override fun onResponse(call: Call<ArrayList<Lot>>, response: Response<ArrayList<Lot>>) {
                    val parkingList = response.body()

                    // 주차장 데이터를 반환하면서 코루틴 재게
                    parkingList?.let { continuation.resumeWith(Result.success(it)) }
                }

                override fun onFailure(call: Call<ArrayList<Lot>>, t: Throwable) {
                    Timber.e("데이터 불러오기 실패 : ${t.message}")
                    continuation.resumeWithException(Exception("데이터를 불러오기에 실패했습니다."))
                }
            })
        }


    // 검색어를 바탕으로 주차장 리스트 플로우를 가져옴
    suspend fun getParkingLotsByQuery(
        query: String?,
        latitude: Double,
        longitude: Double
    ): Flow<ArrayList<Lot>> =

        // 데이터를 가져올 동안, 코루틴 잠시 일시 중지
        suspendCancellableCoroutine { continuation ->
            val api = getRetrofit().create(ParkingLotAPI::class.java)
            api.getLotsByAddress(query, latitude, longitude).enqueue(object : Callback<ArrayList<Lot>> {
                override fun onResponse(call: Call<ArrayList<Lot>>, response: Response<ArrayList<Lot>>) {
                    val parkingList: ArrayList<Lot>? = response.body()
                    parkingList?.forEach { lot ->
                        // 주차장 이름 출력
                        Timber.d("주차장 명 : ${lot.newAddr}")
                    }

                    // 주차장 데이터를 반환하면서 코루틴 재게
                    // 가져온 주차장 리스트를 플로우로 변환
                    parkingList?.let { continuation.resume(flowOf(it), null) }

                }

                override fun onFailure(call: Call<ArrayList<Lot>>, t: Throwable) {
                    Timber.e("데이터 불러오기 실패 : ${t.message}")
                    continuation.resumeWithException(Exception("데이터를 불러오기에 실패했습니다."))
                }
            })
        }

}