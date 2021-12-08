package com.app.service.parking.model.network.retrofit.builder

import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.network.retrofit.api.ParkingLotAPI
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import kotlin.coroutines.resumeWithException

object RetrofitParkingAPIBuilder : BaseRetrofitBuilder() {

    suspend fun getParkingLots(latitude: Double, longitude: Double) =

        // 데이터를 가져올 동안, 코루틴 잠시 일시 중지
        suspendCancellableCoroutine<List<Lot>> { continuation ->
            val api = getRetrofit().create(ParkingLotAPI::class.java)

            // 1페이지의 데이터 10개 가져오기
            api.getLots(latitude, longitude).enqueue(object : Callback<List<Lot>> {
                override fun onResponse(call: Call<List<Lot>>, response: Response<List<Lot>>) {

                    /* val resultCode = response.body()?.response?.header?.resultcode
                     val resultMessage = response.body()?.response?.header?.resultmsg*/

                    val data = response.body()
                    data?.forEach { data ->
                        // 주차장 이름 출력
                        Timber.d("주차장 명 : ${data.parkName}")
                    }

                    // 주차장 데이터를 반환하면서 코루틴 재게
                    data?.let { continuation.resumeWith(Result.success(it)) }
                    /*// result code가 00이면 정상적으로 데이터를 가져옴
                    if (resultCode == "00") {
                        // 주차장 데이터 리스트
                        val parkData = response.body()?.response?.body?.items!! 
                        response.body()?.response?.body?.items?.forEach { data ->
                            // 주차장 이름 출력
                            Timber.d("주차장 명 : ${data.parkName}")
                        }
                        
                        // 주차장 데이터를 반환하면서 코루틴 재게
                        continuation.resumeWith(Result.success(parkData))
                    } else { // 데이터를 정상적으로 가져오지 못했을 때
                        Timber.d("에러 발생 : ${resultCode.toString()}")
                        Timber.d("에러 발생 : ${resultMessage.toString()}")
                        continuation.resumeWithException(Exception("데이터를 불러오기에 실패했습니다."))
                    }*/

                }

                override fun onFailure(call: Call<List<Lot>>, t: Throwable) {
                    Timber.e("데이터 불러오기 실패 : ${t.message}")
                    continuation.resumeWithException(Exception("데이터를 불러오기에 실패했습니다."))
                }
            })
        }

}