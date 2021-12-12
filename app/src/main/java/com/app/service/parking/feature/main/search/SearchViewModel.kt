package com.app.service.parking.feature.main.search

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.app.service.parking.R
import com.app.service.parking.feature.base.BaseViewModel
import com.app.service.parking.global.App
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.preference.ParkingPreference
import com.app.service.parking.model.repository.remote.ParkingLotRepository
import com.app.service.parking.model.type.SearchMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.text.FieldPosition

class SearchViewModel(val repository: ParkingLotRepository) : BaseViewModel() {
    private val debounceTime = 300L
    private val _searchQuery = MutableStateFlow("") // 검색 쿼리 Flow
    val searchMode = MutableLiveData<SearchMode>(SearchMode.TEXT)

    // 검색 쿼리값 설정
    fun setSearchQuery(query: String?) {
        if (query != null) {
            _searchQuery.value = query
        } else {
            _searchQuery.value = ""
        }
    }

    // _searchQuery가 기본값("") 이후에 설정된 값이 있는지 확인
    // 음성인식으로 처음 들어왔을 때, 로딩화면 사라지지 않고 보여주기 위함
    fun isSetQuery(): Boolean {
        return _searchQuery.value != ""
    }

    // 쿼리 Flow 변화에 따라 서버에게 값을 받아와 갱신 (LiveData)
    // 검색 결과 리스트 Live Data
    @ExperimentalCoroutinesApi
    @FlowPreview
    val searchResult = _searchQuery
        .debounce(debounceTime)
        .flatMapLatest { query ->
            // 검색 쿼리가 2글자 이상일 때
            if (query.isNullOrBlank().not() && query.length > 1) {
                getLotsFlow(query) // 주차장 리스트 플로우를 가져온다.
            } else {
                flowOf(ArrayList()) // 값이 비어 있다면 빈 리스트를 반환한다.
            }
        }
        .flowOn(Dispatchers.Default)
        .catch { e: Throwable ->
            e.printStackTrace()
            Timber.d("검색 결과","검색 데이터 가져오기 실패")
        }
        .catch { e: Throwable -> e.printStackTrace() }
        .asLiveData()


    // 검색창에 입력한 값을 바탕으로 서버에서 주차장 데이터를 요청함 
    private suspend fun getLotsFlow(query: String?): Flow<ArrayList<Lot>> {
        // 가까운 주차장 순으로 가져오기 위해 위도, 경도를 파라미터로 전달
        // SharedPreference에서 최근에 있던 위치의 위도, 경도를 가져옴
        val latitude = ParkingPreference.getString(App.context?.getString(R.string.latitude) ?: "latitude", "0.0")?.toDouble()
        val longitude = ParkingPreference.getString(App.context?.getString(R.string.longitude) ?: "longitude", "0.0")?.toDouble()
        return repository.getLotsFlow(query, latitude!!, longitude!!)
    }

    // 검색 모드 변경 아이콘을 클릭했을 때
    fun onClickSearchMode(v: View) {
        searchMode.postValue(searchMode.value?.toggleMode())
    }
    
    // 아이템 삭제 버튼을 클릭했을 때
    @SuppressLint("LongLogTag")
    fun deleteItem(position: Int) {
        Log.d("deleteItem(position: Int)", searchResult.value?.get(position)?.parkName.toString())
        //searchResult.value[position]
    }
}