package com.app.service.parking.feature.main.review

import androidx.lifecycle.*
import com.app.service.parking.feature.base.BaseViewModel
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.repository.entity.Favorite
import com.app.service.parking.model.repository.local.repository.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewViewModel(val repository: FavoriteRepository) : BaseViewModel() {

    class Factory(val repository: FavoriteRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ReviewViewModel(repository) as T
        }
    }

    // 즐겨찾기 DB에서 가져올 Lot 데이터
    // 즐겨찾기를 안 해서 Null일 수 있는 상황을 가정하여 Nullable
    val favoriteLot = MutableLiveData<Favorite?>()
    // 즐겨찾기 여부
    val isFavorite = MutableLiveData(false)

    // 즐겨찾기 데이터
    private var allUser: LiveData<List<Favorite>> = repository.allFavorite
    // 주차장 데이터 모델
    var lotModel: Lot? = null

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

            repository.insert(entity)
        }


    }

    fun getByParkCode() = viewModelScope.launch(Dispatchers.IO) {
        favoriteLot.postValue(repository.selectByParkCode(lotModel!!.parkCode))
    }
    
    // 주차장 즐겨찾기 삭제
    fun deleteLot(entity: Favorite) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(entity)
    }

    // 주차장 즐겨찾기 데이터 가져오기
    fun getAllFavorites(): LiveData<List<Favorite>> {
        return allUser
    }
}