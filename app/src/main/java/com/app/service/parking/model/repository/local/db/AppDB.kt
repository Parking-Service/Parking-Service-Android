package com.app.service.parking.model.repository.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.service.parking.model.repository.local.DBConst
import com.app.service.parking.model.repository.local.dao.DAOFavorite
import com.app.service.parking.model.repository.entity.Favorite


@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {

    abstract fun getDAOFavorite(): DAOFavorite

    companion object {
        @Volatile
        private var INSTANCE: AppDB? = null

        @Synchronized
        fun getDatabase(
            context: Context,
        ): AppDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDB::class.java,
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