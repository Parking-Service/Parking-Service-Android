package com.app.service.parking.model.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.service.parking.R
import com.app.service.parking.global.App
import com.app.service.parking.model.repository.local.DBConst
import java.io.Serializable

@Entity(tableName = DBConst.TABLE_NAME_USER_FAVORITE)
data class Favorite(
    @PrimaryKey
    @ColumnInfo(name=("prkplceNo")) // 주차장관리번호 (eg. 156-2-000043)
    var parkCode: String,
    @ColumnInfo(name=("prkplceNm")) // 주차장명
    var parkName: String,
    @ColumnInfo(name=("rdnmadr")) // 도로명 주소
    var newAddr: String? = App.context?.getString(R.string.no_info),
    @ColumnInfo(name=("lnmadr")) // 일반 주소
    var oldAddr: String? = App.context?.getString(R.string.no_info),
    @ColumnInfo(name=("operDay"))
    var operDay: String,
    @ColumnInfo(name=("weekdayOperOpenHhmm"))
    var weekdayOpenTime: String,
    @ColumnInfo(name=("weekdayOperCloseHhmm"))
    var weekdayCloseTime: String,
    @ColumnInfo(name=("satOperOperOpenHhmm"))
    var saturdayOpenTime: String,
    @ColumnInfo(name=("satOperCloseHhmm"))
    var saturdayCloseTime: String,
    @ColumnInfo(name=("holidayOperOpenHhmm"))
    var holidayOpenTime: String,
    @ColumnInfo(name=("holidayCloseOpenHhmm"))
    var holidayCloseTime: String,
    @ColumnInfo(name=("parkingchrgeInfo")) // 요금 정보 (무료/유료)
    var feeType: String,
    @ColumnInfo(name=("basicTime")) // 주차기본시간
    var basicParkTime: String,
    @ColumnInfo(name=("basicCharge")) // 주차기본요금
    var basicFee: String = "0",
    @ColumnInfo(name=("addUnitTime")) // 추가단위시간
    var addUnitTime: String?,
    @ColumnInfo(name=("addUnitCharge")) // 추가단위요금
    var addUnitFee: String?,
    @ColumnInfo(name=("dayCmmtktAdjTime")) // 1일주차권요금적용시간
    var parkTimePerDay: String?,
    @ColumnInfo(name=("dayCmmtkt")) // 1일주차권요금
    var feePerDay: String?,
    @ColumnInfo(name=("monthCmmtkt")) // 월정기권요금
    var feePerMonth: String,
    @ColumnInfo(name=("metpay")) // 결제방법 (현금 / 현금, 카드 / 카드)
    var payType: String,
    @ColumnInfo(name=("spcmnt")) // 특기사항(특이사항)
    var uniqueness: String,
    @ColumnInfo(name=("phoneNumber")) // 전화번호
    var phoneNumber: String,
    @ColumnInfo(name=("latitude")) // 위도
    var latitude: String,
    @ColumnInfo(name=("longitude")) // 경도
    var longitude: String
): Serializable