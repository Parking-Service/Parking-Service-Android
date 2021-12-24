package com.app.service.parking.model.repository.local.repository

import androidx.lifecycle.LiveData
import com.app.service.parking.model.repository.local.db.AppDB
import com.app.service.parking.model.repository.entity.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteRepository(mDatabase: AppDB) {

    private val dao = mDatabase.getDAOFavorite()
    val allFavorite: LiveData<List<Favorite>> = dao.getAll()

    companion object {
        private var instance: FavoriteRepository? = null

        fun getInstance(database: AppDB): FavoriteRepository? {
            return instance ?: synchronized(this) {
                val _instance = FavoriteRepository(database)
                instance = _instance
                instance
            }
        }
    }

    fun selectByParkCode(parkCode: String): Favorite {
        return dao.selectByParkCode(parkCode)
    }

    fun insert(entity: Favorite) {
        dao.insert(entity)
    }

    fun delete(entity: Favorite) {
        dao.delete(entity)
    }
}