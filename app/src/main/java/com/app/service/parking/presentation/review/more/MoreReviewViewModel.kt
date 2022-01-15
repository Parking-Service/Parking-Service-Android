package com.app.service.parking.presentation.review.more

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.dto.Review
import com.app.service.parking.model.repository.remote.ReviewRepository
import com.app.service.parking.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class MoreReviewViewModel(private val reviewRepository: ReviewRepository) : BaseViewModel() {

    // 주차장 데이터 모델
    var lotModel: Lot? = null

    // 전체 주차장 리뷰 리스트
    val allReviewList = MutableLiveData<ArrayList<Review>>()

    // '좋아요' 주차장 리뷰 리스트
    val goodReviewList = MutableLiveData<List<Review>>()

    // '평범해요' 주차장 리뷰 리스트
    val normalReviewList = MutableLiveData<List<Review>>()

    // '나빠요' 주차장 리뷰 리스트
    val badReviewList = MutableLiveData<List<Review>>()

    // 각 리뷰 타입별로 리뷰의 개수를 반환하는 함수
    fun getReviewCount(position: Int): Int {
        return when (position) {
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
            allReviewList.value = reviewRepository.getAllReviewList(lotModel?.parkCode!!) // 리뷰 데이터 리스트 갱신
            goodReviewList.value = getGoodReviewList() // 좋아요 리뷰 리스트 MutableLiveData 설정
            normalReviewList.value = getNormalReviewList() // 평범해요 리뷰 리스트 MutableLiveData 설정
            badReviewList.value = getBadReviewList() // 나빠요 리뷰 리스트 MutableLiveData 설정
        }
    }

    private fun getGoodReviewList() = allReviewList.value?.filter { review ->
        review.reviewRate == 3.0F
    }


    private fun getNormalReviewList() = allReviewList.value?.filter { review ->
        review.reviewRate == 2.0F
    }


    private fun getBadReviewList() = allReviewList.value?.filter { review ->
        review.reviewRate == 1.0F
    }

}