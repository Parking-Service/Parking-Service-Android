package com.app.service.parking.feature.main

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.service.parking.feature.base.BaseViewModel
import com.app.service.parking.feature.main.search.SearchActivity
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.network.retrofit.builder.RetrofitParkingAPIBuilder
import com.app.service.parking.model.type.LocationFabStatus
import com.app.service.parking.util.GPSStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainViewModel : BaseViewModel() {

    private val _location = MutableStateFlow(Pair<Double, Double>(0.0, 0.0))
    val fabStatus = MutableLiveData<LocationFabStatus>() // fab버튼 상태 라이브 데이터
    var isMovedMyLocation = false
    var backPressInterval = 2000 // 뒤로가기 시간 interval
    var backPressTime: Long = 0L // 뒤로가기를 마지막으로 누른 시간
    private val debounceTime = 400L // Debounce Search를 위한 변수 (flatMapLatest와 함께 사용하면 가장 마지막에 전달된 값으로 로직 수행)

    // 위도 지정
    fun setLocation(locationPair: Pair<Double, Double>) {
        _location.value = locationPair
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    var lotData = _location
        .debounce(debounceTime)
        .flatMapLatest { location ->
            val latitude = location.first
            val longitude = location.second
            if (latitude != 0.0 && longitude != 0.0) {
                flowOf(getLotData(latitude, longitude))
            } else {
                flowOf(ArrayList())
            }
        }
        .flowOn(Dispatchers.IO)
        .catch { e: Throwable ->
            e.printStackTrace()
            Timber.d("검색 결과", "검색 데이터 가져오기 실패")
        }.asLiveData()


    // 주차장 데이터 가져오는 함수
    // 입력받은 위도, 경도 기반으로 서버에게 주차장 데이터 요청
    private suspend fun getLotData(latitude: Double, longitude: Double): ArrayList<Lot>? {
        try {
            val data = withContext(viewModelScope.coroutineContext) {
                RetrofitParkingAPIBuilder.getParkingLots(latitude, longitude)
            }
            return data
        } catch (e: Exception) {
            Timber.d("에러 발생으로인해 데이터 불러오기를 중단했습니다.")
        }
        return null
    }

    // Fab 버튼을 눌렀을 때,
    fun onClickLocationFab(view: View) {
        if (GPSStatus.checkGPS(view)) {
            // 다음 Location Fab 버튼의 상태를 가져와서 fabStatus 변수를 업데이트해준다.
            val nextStatus = fabStatus.value?.getNextStatus()
            fabStatus.value = (nextStatus!!)
        }
    }

    fun onClickSearchBar(view: View) {
        startActivity(SearchActivity::class, null)
    }
}