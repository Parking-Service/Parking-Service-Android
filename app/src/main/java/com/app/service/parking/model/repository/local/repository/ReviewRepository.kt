package com.app.service.parking.model.repository.local.repository

import androidx.lifecycle.LiveData
import androidx.room.Entity
import com.app.service.parking.model.repository.local.db.ReviewDatabase

class ReviewRepository(mDatabase: ReviewDatabase) {

    private val dao = mDatabase.getDAOFavorite()
    val allFavorite: LiveData<List<Entity>> = dao.getAll()

    companion object {
        private var instance: ReviewRepository? = null

        fun getInstance(database: ReviewDatabase): ReviewRepository? {
            return instance ?: synchronized(this) {
                val _instance = ReviewRepository(database)
                instance = _instance
                instance
            }
        }
    }

    suspend fun insert(entity: Entity) {
        dao.insert(entity)
    }

    suspend fun delete(entity: Entity) {
        dao.delete(entity)
    }
}