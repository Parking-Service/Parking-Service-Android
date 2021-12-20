package com.app.service.parking.model.repository.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.service.parking.model.repository.local.DBConst
import com.app.service.parking.model.repository.local.dao.DAOFavorite
import com.app.service.parking.model.repository.local.entity.EntityFavorite
import kotlinx.coroutines.CoroutineScope


@Database(entities = [EntityFavorite::class], version = 1, exportSchema = false)
abstract class ReviewDatabase : RoomDatabase() {

    abstract fun getDAOFavorite(): DAOFavorite

    companion object {
        @Volatile
        private var INSTANCE: ReviewDatabase? = null
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ReviewDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReviewDatabase::class.java,
                    DBConst.DATABASE_NAME_REVIEW
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                // return instance
                instance
            }
        }
    }
}