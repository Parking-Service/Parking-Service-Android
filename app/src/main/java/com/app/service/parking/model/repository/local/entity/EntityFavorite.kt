package com.app.service.parking.model.repository.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.repository.local.DBConst

@Entity(tableName = DBConst.TABLE_NAME_USER_FAVORITE)
data class EntityFavorite(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var lot: Lot
)