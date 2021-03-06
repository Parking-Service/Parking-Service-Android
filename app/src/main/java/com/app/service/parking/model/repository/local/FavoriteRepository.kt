package com.app.service.parking.model.repository.local

import androidx.lifecycle.LiveData
import com.app.service.parking.model.repository.entity.Favorite
import com.app.service.parking.model.db.AppDB

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