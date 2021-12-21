package com.app.service.parking.feature.main.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.service.parking.feature.base.BaseViewModel
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.repository.local.entity.EntityFavorite
import com.app.service.parking.model.repository.local.repository.FavoriteRepository
import kotlinx.coroutines.launch

class ReviewViewModel(val repository: FavoriteRepository) : BaseViewModel() {

    class Factory(val repository: FavoriteRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ReviewViewModel(repository) as T
        }
    }

    // 즐겨찾기 데이터
    private var allUser: LiveData<List<EntityFavorite>> = repository.allFavorite
    // 주차장 데이터 모델
    var lotModel: Lot? = null

    // 주차장 즐겨찾기 추가
    fun insertFavoriteLot(entity: EntityFavorite) = viewModelScope.launch {
        repository.insert(entity)
    }
    
    // 주차장 즐겨찾기 삭제
    fun deleteFavoriteLot(entity: EntityFavorite) = viewModelScope.launch {
        repository.delete(entity)
    }

    // 주차장 즐겨찾기 데이터 가져오기
    fun getAllFavorites(): LiveData<List<EntityFavorite>> {
        return allUser
    }
}