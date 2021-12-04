package com.app.buna.foodplace.model.network.retrofit.builder

import android.util.Log
import com.app.buna.foodplace.model.dto.ParkingLot
import com.app.buna.foodplace.model.network.retrofit.api.ParkingLotAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

object RetrofitParkingAPIBuilder : BaseRetrofitBuilder() {

    // base url을 공공 데이터 포털 사이트로 지정
    override val baseUrl: String
        get() = "http://api.data.go.kr"
    const val API_KEY = "dBA13HtoNsD/e5rHRalqhisQmGXh5FfOTGpqyCIfm0ZcGO7qEKGcabtsEEIf5k1GlFFStcue18GiGGWjgaQEdw=="

    fun getParkingLots() {
        val api = getRetrofit().create(ParkingLotAPI::class.java)

        // 1페이지의 데이터 10개 가져오기
        api.getParkingLot(API_KEY, prkplceNm = "송현주공시장 공영 주차장").enqueue(object : Callback<ParkingLot> {
            override fun onResponse(call: Call<ParkingLot>, response: Response<ParkingLot>) {
                // result code가 00이면 정상
                val resultCode = response.body()?.response?.header?.resultcode
                val resultMessage = response.body()?.response?.header?.resultmsg

                if(resultCode == "00") {
                    response.body()?.response?.body?.items?.forEach { data ->
                        // 주차장 이름 출력
                        Timber.d("주차장 명 : ${data.prkplcenm}")
                    }
                } else { // 데이터를 정상적으로 가져오지 못했을 때
                    Timber.d("에러 발생 : ${resultCode.toString()}")
                    Timber.d("에러 발생 : ${resultMessage.toString()}")
                }

            }

            override fun onFailure(call: Call<ParkingLot>, t: Throwable) {
                Timber.e("데이터 불러오기 실패 : ${t.message}")
            }
        })
    }
}