package com.app.service.parking.presentation.review.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.service.parking.presentation.base.BaseViewModel
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.dto.Review
import com.app.service.parking.model.preference.ParkingPreference
import com.app.service.parking.model.preference.PreferenceConst
import com.app.service.parking.model.repository.remote.ReviewRepository
import kotlinx.coroutines.launch

class AllReviewViewModel(val reviewRepository: ReviewRepository): BaseViewModel() {

    // 주차장 데이터 모델
    var lotModel: Lot? = null

    // 주차장 리뷰 리스트
    val reviewList = MutableLiveData<ArrayList<Review>>()

    // 각 리뷰 타입별로 리뷰의 개수를 반환하는 함수
    fun getReviewCount(position: Int): Int {
        return when(position) {
            0 -> 10
            1 -> 20
            2 -> 30
            3 -> 40
            else -> 0
        }
    }

    // 서버로부터 리뷰 리스트를 요청한다.
    fun requestReviewList() {
        viewModelScope.launch {
            reviewList.value = reviewRepository.getReviewList(lotModel?.parkCode!!) // 리뷰 데이터 리스트 갱신
        }
    }
}