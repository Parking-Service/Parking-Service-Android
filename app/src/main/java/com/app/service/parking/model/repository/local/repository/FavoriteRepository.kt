package com.app.service.parking.model.repository.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.service.parking.model.repository.local.db.AppDB
import com.app.service.parking.model.repository.local.entity.EntityFavorite

class FavoriteRepository(mDatabase: AppDB) {

    private val dao = mDatabase.getDAOFavorite()
    val allFavorite: LiveData<List<EntityFavorite>> = dao.getAll()

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

    suspend fun insert(entity: EntityFavorite) {
        dao.insert(entity)
    }

    suspend fun delete(entity: EntityFavorite) {
        dao.delete(entity)
    }
}