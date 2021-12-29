package com.app.service.parking.feature.main.review

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.app.service.parking.feature.base.BaseViewModel
import com.app.service.parking.model.type.RateStatus

class ReviewWriteViewModel: BaseViewModel() {
    val imgUriList = mutableListOf<Uri>() // 갤러리에서 가져온 이미지 피커 Uri 리스트
    val rateStatus = MutableLiveData<RateStatus>(RateStatus.GOOD) // 평점 상태
    var reviewText = MutableLiveData<String>("")

    // 평점 상태 변경
    fun changeRateStatus(changedRateStatus: Int) {
        rateStatus.value = when(changedRateStatus) {
            0 -> RateStatus.GOOD
            1 -> RateStatus.NORMAL
            2 -> RateStatus.BAD
            else -> RateStatus.GOOD
        }
    }
}