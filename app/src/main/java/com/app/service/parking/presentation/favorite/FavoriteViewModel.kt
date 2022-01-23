package com.app.service.parking.presentation.favorite

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.service.parking.presentation.base.BaseViewModel
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.repository.entity.Favorite
import com.app.service.parking.model.repository.local.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(val repository: FavoriteRepository): BaseViewModel() {

    class Factory(val repository: FavoriteRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(repository) as T
        }
    }
    
    // Room DB에서 가져온 Entity 리스트
    var favoriteEntityList: LiveData<List<Favorite>> = repository.allFavorite
    // Room DB에서 가져온 Entitiy 리스트를 Lot리스트로 변형한 변수
    val favoriteLotList = ArrayList<Lot>()

    // 아이템 삭제 버튼을 클릭했을 때
    @SuppressLint("LongLogTag")
    fun deleteItem(position: Int) {
        // 선택한 주차장 데이터 Entity 삭제
        viewModelScope.launch(Dispatchers.IO) {
            favoriteEntityList.value?.get(position)?.let { repository.delete(it) }
        }
        //searchResult.value[position]
    }

}