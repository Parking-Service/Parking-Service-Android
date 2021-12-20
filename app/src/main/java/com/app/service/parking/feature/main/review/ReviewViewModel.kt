package com.app.service.parking.feature.main.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Entity
import com.app.service.parking.feature.base.BaseViewModel
import com.app.service.parking.global.App
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.repository.local.db.ReviewDatabase
import com.app.service.parking.model.repository.local.repository.ReviewRepository
import kotlinx.coroutines.launch

class ReviewViewModel : BaseViewModel() {
    // Local Repository
    val repository: ReviewRepository = ReviewRepository(ReviewDatabase.getDatabase(App.context!!, viewModelScope))
    // 즐겨찾기 데이터
    private var allUser: LiveData<List<Entity>> = repository.allFavorite
    // 주차장 데이터 모델
    var lotModel: Lot? = null

    // 주차장 즐겨찾기 추가
    fun insertFavorite(entity: Entity) = viewModelScope.launch {
        repository.insert(entity)
    }
    
    // 주차장 즐겨찾기 삭제
    fun deleteFavorite(entity: Entity) = viewModelScope.launch { 
        repository.delete(entity)
    }

    // 주차장 즐겨찾기 데이터 가져오기
    fun getAllFavorite(): LiveData<List<Entity>> {
        return allUser
    }
}