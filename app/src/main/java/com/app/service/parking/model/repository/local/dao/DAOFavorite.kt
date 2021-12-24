package com.app.service.parking.model.repository.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.service.parking.model.repository.local.DBConst
import com.app.service.parking.model.repository.entity.Favorite


@Dao
interface DAOFavorite {

    // 데이터 베이스 불러오기
    @Query("SELECT * FROM ${DBConst.TABLE_NAME_USER_FAVORITE}")
    fun getAll(): LiveData<List<Favorite>>

    // 데이터 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: Favorite)

    // 데이터 전체 삭제
    @Query("DELETE FROM ${DBConst.TABLE_NAME_USER_FAVORITE}")
    fun deleteAll()

    // 데이터 업데이트
    @Update
    fun update(entity: Favorite)

    // 데이터 삭제
    @Delete
    fun delete(entity: Favorite)

    // 주차장 코드로 데이터 가져오기
    @Query("SELECT * FROM ${DBConst.TABLE_NAME_USER_FAVORITE} WHERE prkplceNo = :parkCode")
    fun selectByParkCode(parkCode: String): Favorite


}