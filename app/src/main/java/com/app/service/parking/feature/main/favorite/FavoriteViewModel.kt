package com.app.service.parking.feature.main.favorite

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.service.parking.feature.base.BaseViewModel
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.repository.local.repository.FavoriteRepository

class FavoriteViewModel(val repository: FavoriteRepository): BaseViewModel() {

    class Factory(val repository: FavoriteRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(repository) as T
        }
    }

    val searchResult = MutableLiveData<ArrayList<Lot>>()

    // 아이템 삭제 버튼을 클릭했을 때
    @SuppressLint("LongLogTag")
    fun deleteItem(position: Int) {
        Log.d("deleteItem(position: Int)", searchResult.value?.get(position)?.parkName.toString())
        //searchResult.value[position]
    }

}