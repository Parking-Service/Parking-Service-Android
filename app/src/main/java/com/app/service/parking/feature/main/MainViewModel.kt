package com.app.service.parking.feature.main

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.service.parking.feature.base.BaseViewModel
import com.app.service.parking.feature.main.search.SearchActivity
import com.app.service.parking.model.type.LocationFabStatus
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.network.retrofit.builder.RetrofitParkingAPIBuilder
import com.app.service.parking.util.GPSStatus
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class MainViewModel : BaseViewModel() {


    val lotData = MutableLiveData<List<Lot>>() // 주차장 데이터
    val fabStatus = MutableLiveData<LocationFabStatus>() // fab버튼 상태 라이브 데이터
    var isMovedMyLocation = false
    var backPressInterval = 2000 // 뒤로가기 시간 interval
    var backPressTime: Long = 0L // 뒤로가기를 마지막으로 누른 시간

    // 주차장 데이터 가져오는 함수
    // 입력받은 위도, 경도 기반으로 서버에게 주차장 데이터 요청
    suspend fun getLotData(latitude: Double, longitude: Double) {
        try {
            val data = withContext(viewModelScope.coroutineContext) {
                RetrofitParkingAPIBuilder.getParkingLots(latitude, longitude)
            }

            lotData.postValue(data)
        }catch (e: Exception) {
            Timber.d("에러 발생으로인해 데이터 불러오기를 중단했습니다.")
        }
    }

    // Fab 버튼을 눌렀을 때,
    fun onClickLocationFab(view: View) {
        if(GPSStatus.checkGPS(view)) {
            // 다음 Location Fab 버튼의 상태를 가져와서 fabStatus 변수를 업데이트해준다.
            val nextStatus = fabStatus.value?.getNextStatus()
            fabStatus.value = (nextStatus!!)
        }
    }

    fun onClickSearchBar(view: View) {
        startActivity(SearchActivity::class, null)
    }
}