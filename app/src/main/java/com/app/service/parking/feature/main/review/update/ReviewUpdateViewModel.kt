package com.app.service.parking.feature.main.review.update

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.service.parking.feature.base.BaseViewModel
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.dto.Review
import com.app.service.parking.model.preference.ParkingPreference
import com.app.service.parking.model.preference.PreferenceConst
import com.app.service.parking.model.repository.remote.ReviewRepository
import com.app.service.parking.model.type.RateStatus
import kotlinx.coroutines.launch

class ReviewUpdateViewModel(val repository: ReviewRepository) : BaseViewModel() {
    val imgUriLiveList = MutableLiveData<MutableList<Uri>>() // 갤러리에서 가져온 이미지 피커 LiveData Uri 리스트
    val imgUriList = mutableListOf<Uri>() // 갤러리에서 가져온 이미지 피커 Uri 리스트
    val rateStatus = MutableLiveData<RateStatus>(RateStatus.GOOD) // 평점 상태
    var reviewText = MutableLiveData<String>("")
    val imageOriginMaxCount = 5 // 이미지 최대 선택 개수
    val isUploadSuccess = MutableLiveData<Boolean>()

    // 평점 상태 변경
    fun changeRateStatus(changedRateStatus: Int) {
        rateStatus.value = when (changedRateStatus) {
            0 -> RateStatus.GOOD
            1 -> RateStatus.NORMAL
            2 -> RateStatus.BAD
            else -> RateStatus.GOOD
        }
    }

    // 리뷰를 업로드하는 메서드
    fun uploadReview(lot: Lot) {
        // 평점을 수치화한다
        val rateValue: Float = when (rateStatus.value) {
            RateStatus.GOOD -> 3.0f // 좋아요 -> 5점
            RateStatus.NORMAL -> 2.0f // 평범해요 -> 3점
            RateStatus.BAD -> 1.0f // 별로에요 -> 1점
            else -> 3.0f // Default -> 5점
        }

        // 주차장 리뷰 데이터를 서버에 전송하여 리뷰를 등록한다.
        viewModelScope.launch {
            try {
                // 레퍼지토리에 접근하여 리뷰 데이터 등록
                repository.upload(
                    Review(
                        reviewerUid = ParkingPreference.getString(PreferenceConst.UID.name), // Preference로부터 유저 Uid를 가져온다.
                        parkCode = lot.parkCode, // 주차장 코드
                        reviewerNickName = ParkingPreference.getString(PreferenceConst.NICKNAME.name), // Preference로부터 유저 닉네임을 가져온다.
                        reviewRate = rateValue, // 주차장 평점
                        reviewText = reviewText.value // 주차장에 대한 리뷰
                    ), imgUriList.toList())
                isUploadSuccess.value = true // 결과 반환시 프로그레스바를 멈추기 위해 관찰 데이터 변경
            }catch (e: Exception) {
                e.printStackTrace()
                isUploadSuccess.value = false // 결과 반환시 프로그레스바를 멈추기 위해 관찰 데이터 변경
            }
        }
    }

}