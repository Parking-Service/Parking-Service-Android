package com.app.service.parking.feature.main.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.service.parking.feature.base.BaseViewModel
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.dto.Review
import com.app.service.parking.model.repository.entity.Favorite
import com.app.service.parking.model.repository.local.repository.FavoriteRepository
import com.app.service.parking.model.repository.remote.ReviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val reviewRepository: ReviewRepository,
    private val favoriteRepository: FavoriteRepository
) : BaseViewModel() {

    // 즐겨찾기 DB에서 가져올 Lot 데이터
    // 즐겨찾기를 안 해서 Null일 수 있는 상황을 가정하여 Nullable
    val favoriteLot = MutableLiveData<Favorite?>()

    // 즐겨찾기 여부
    val isFavorite = MutableLiveData(false)

    // 즐겨찾기 데이터
    private var allUser: LiveData<List<Favorite>> = favoriteRepository.allFavorite

    // 주차장 데이터 모델
    var lotModel: Lot? = null

    // 주차장 리뷰 리스트
    val userReviewList = MutableLiveData<ArrayList<Review>>()

    // 주차장 즐겨찾기 추가
    fun insertLot() = viewModelScope.launch(Dispatchers.IO) {
        with(lotModel) {
            val entity = Favorite(
                this?.parkCode!!,
                parkName,
                newAddr,
                oldAddr,
                operDay,
                weekdayOpenTime,
                weekdayCloseTime,
                saturdayOpenTime,
                saturdayCloseTime,
                holidayOpenTime,
                holidayCloseTime,
                feeType,
                basicParkTime,
                basicFee,
                addUnitTime,
                addUnitFee,
                parkTimePerDay,
                feePerDay,
                feePerMonth,
                payType,
                uniqueness,
                phoneNumber,
                latitude,
                longitude
            )

            favoriteRepository.insert(entity)
        }


    }

    fun getByParkCode() = viewModelScope.launch(Dispatchers.IO) {
        favoriteLot.postValue(favoriteRepository.selectByParkCode(lotModel!!.parkCode))
    }

    // 주차장 즐겨찾기 삭제
    fun deleteLot(entity: Favorite) = viewModelScope.launch(Dispatchers.IO) {
        favoriteRepository.delete(entity)
    }

    // 주차장 즐겨찾기 데이터 가져오기
    fun getAllFavorites(): LiveData<List<Favorite>> {
        return allUser
    }

    // 서버로부터 리뷰 리스트를 요청한다.
    fun requestReviewList() {
        viewModelScope.launch {
            userReviewList.value = reviewRepository.getReviewList(lotModel?.parkCode!!)
        }
    }
}